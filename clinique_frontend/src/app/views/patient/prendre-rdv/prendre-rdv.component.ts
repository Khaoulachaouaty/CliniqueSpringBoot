// views/patient/prendre-rdv/prendre-rdv.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { RendezVousService } from '../../../services/rendezvous.service';
import { ApiService } from '../../../services/api.service';
import { AuthService } from '../../../services/auth.service';
import { Medecin } from '../../../models/medecin.model';
import { RendezVousRequest } from '../../../models/rendezvous.model';

@Component({
  selector: 'app-prendre-rdv',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, RouterLink],
  templateUrl: './prendre-rdv.component.html',
  styleUrls: ['./prendre-rdv.component.css']
})
export class PrendreRdvComponent implements OnInit {
  etape = 1;
  medecins: Medecin[] = [];
  medecinsFiltres: Medecin[] = [];
  specialites: string[] = [];
  
  // Filtres
  specialiteSelectionnee = '';
  rechercheMedecin = '';
  
  medecinSelectionne: Medecin | null = null;
  dateSelectionnee = '';
  creneauSelectionne = '';
  creneauxDisponibles: string[] = [];
  creneauxOccupes: string[] = [];
  
  // 🔥 ÉTATS DE CHARGEMENT SÉPARÉS
  loadingCreneaux = false;
  loadingOccupes = false;
  isLoading = false;
  
  errorMessage = '';
  dateMin = new Date().toISOString().split('T')[0];
  
  rdvForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private apiService: ApiService,
    private rdvService: RendezVousService,
    private authService: AuthService,
    private router: Router
  ) {
    this.rdvForm = this.fb.group({
      motif: ['', [Validators.required, Validators.minLength(5)]]
    });
  }

  ngOnInit(): void {
    this.loadMedecins();
  }

  loadMedecins(): void {
    this.apiService.getPublicMedecins().subscribe({
      next: (data) => {
        this.medecins = data;
        this.medecinsFiltres = [...data];
        this.specialites = [...new Set(data.map(m => m.specialite))].filter(Boolean).sort();
      },
      error: () => this.errorMessage = 'Erreur lors du chargement des médecins'
    });
  }

  filtrerMedecins(): void {
    this.medecinsFiltres = this.medecins.filter(m => {
      const matchSpecialite = !this.specialiteSelectionnee || m.specialite === this.specialiteSelectionnee;
      const matchRecherche = !this.rechercheMedecin || 
        `${m.prenom} ${m.nom}`.toLowerCase().includes(this.rechercheMedecin.toLowerCase()) ||
        m.specialite.toLowerCase().includes(this.rechercheMedecin.toLowerCase());
      return matchSpecialite && matchRecherche;
    });
  }

  onSpecialiteChange(): void {
    this.filtrerMedecins();
  }

  onRechercheChange(): void {
    this.filtrerMedecins();
  }

  selectionnerMedecin(medecin: Medecin): void {
    this.medecinSelectionne = medecin;
    // Réinitialiser la date et les créneaux si on change de médecin
    this.dateSelectionnee = '';
    this.creneauSelectionne = '';
    this.creneauxDisponibles = [];
    this.creneauxOccupes = [];
  }

  onDateChange(): void {
    if (!this.dateSelectionnee || !this.medecinSelectionne) return;
    
    // 🔥 BLOQUER L'AFFICHAGE JUSQU'À CE QUE TOUS LES DONNÉES SOIENT CHARGÉES
    this.loadingCreneaux = true;
    this.loadingOccupes = true;
    this.creneauSelectionne = '';
    this.creneauxOccupes = [];
    
    // Charger d'abord les créneaux disponibles
    this.rdvService.getCreneauxDisponibles(this.medecinSelectionne.id, this.dateSelectionnee)
      .subscribe({
        next: (creneaux) => {
          this.creneauxDisponibles = creneaux;
          // Puis charger les créneaux occupés
          this.loadCreneauxOccupes();
        },
        error: () => {
          this.errorMessage = 'Erreur lors du chargement des créneaux';
          this.loadingCreneaux = false;
          this.loadingOccupes = false;
        }
      });
  }

  loadCreneauxOccupes(): void {
    if (!this.medecinSelectionne || !this.dateSelectionnee) {
      this.loadingCreneaux = false;
      this.loadingOccupes = false;
      return;
    }
    
    this.rdvService.getCreneauxOccupes(this.medecinSelectionne.id, this.dateSelectionnee)
      .subscribe({
        next: (occupes) => {
          this.creneauxOccupes = occupes;
          // 🔥 NE PAS AFFICHER AVANT D'AVOIR LES DEUX LISTES
          this.loadingCreneaux = false;
          this.loadingOccupes = false;
          console.log('✅ Créneaux chargés:', { 
            disponibles: this.creneauxDisponibles.length, 
            occupes: this.creneauxOccupes.length 
          });
        },
        error: () => {
          this.loadingCreneaux = false;
          this.loadingOccupes = false;
          this.errorMessage = 'Erreur lors du chargement des créneaux occupés';
        }
      });
  }

  isCreneauOccupe(creneau: string): boolean {
    return this.creneauxOccupes.includes(creneau);
  }

  selectionnerCreneau(creneau: string): void {
    if (this.isCreneauOccupe(creneau)) {
      // 🔥 DOUBLE VÉRIFICATION - NE DEVRAIT JAMAIS ARRIVER AVEC LA CORRECTION
      this.errorMessage = 'Ce créneau est déjà réservé. Veuillez en choisir un autre.';
      return;
    }
    this.creneauSelectionne = creneau;
    this.errorMessage = ''; // Effacer les erreurs précédentes
  }

  confirmerRdv(): void {
    if (this.rdvForm.invalid || !this.medecinSelectionne || !this.dateSelectionnee || !this.creneauSelectionne) {
      this.rdvForm.markAllAsTouched();
      return;
    }

    // 🔥 VÉRIFICATION FINALE AVANT ENVOI
    if (this.isCreneauOccupe(this.creneauSelectionne)) {
      this.errorMessage = 'Ce créneau vient d\'être réservé par un autre patient. Veuillez en choisir un autre.';
      // Rafraîchir les créneaux
      this.onDateChange();
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const patientId = this.authService.currentUser()?.userId;
    if (!patientId) {
      this.errorMessage = 'Erreur: Patient non identifié. Veuillez vous reconnecter.';
      this.isLoading = false;
      return;
    }

    const request: RendezVousRequest = {
      patientId: patientId,
      medecinId: this.medecinSelectionne.id,
      date: this.dateSelectionnee,
      heure: this.creneauSelectionne,
      motif: this.rdvForm.value.motif
    };

    this.rdvService.createRendezVous(request).subscribe({
      next: () => {
        this.isLoading = false;
        this.etape = 4; // Succès
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Erreur lors de la création du rendez-vous. Veuillez réessayer.';
        // Si erreur de créneau occupé, rafraîchir
        if (err.error?.message?.includes('déjà réservé')) {
          this.onDateChange();
        }
      }
    });
  }

  reset(): void {
    this.etape = 1;
    this.medecinSelectionne = null;
    this.specialiteSelectionnee = '';
    this.rechercheMedecin = '';
    this.medecinsFiltres = [...this.medecins];
    this.dateSelectionnee = '';
    this.creneauSelectionne = '';
    this.creneauxDisponibles = [];
    this.creneauxOccupes = [];
    this.errorMessage = '';
    this.rdvForm.reset();
  }

  retourEtape1(): void {
    this.etape = 1;
    this.medecinSelectionne = null;
    this.dateSelectionnee = '';
    this.creneauSelectionne = '';
    this.creneauxDisponibles = [];
    this.creneauxOccupes = [];
  }

  retourEtape2(): void {
    this.etape = 2;
    this.creneauSelectionne = '';
  }

  // 🔥 GETTERS UTILES POUR LE TEMPLATE
  get creneauxLibres(): number {
    return this.creneauxDisponibles.length - this.creneauxOccupes.length;
  }

  get pourcentageOccupation(): number {
    if (this.creneauxDisponibles.length === 0) return 0;
    return Math.round((this.creneauxOccupes.length / this.creneauxDisponibles.length) * 100);
  }
}