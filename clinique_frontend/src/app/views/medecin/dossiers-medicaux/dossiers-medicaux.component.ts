import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RendezVousService } from '../../../services/rendezvous.service';
import { AuthService } from '../../../services/auth.service';
import { Consultation, RendezVous } from '../../../models/rendezvous.model';

@Component({
  selector: 'app-dossiers-medicaux',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './dossiers-medicaux.component.html',
  styleUrls: ['./dossiers-medicaux.component.css']
})
export class DossiersMedicauxComponent implements OnInit {
  
  patients: any[] = [];
  selectedPatient: any = null;
  patientRendezVous: RendezVous[] = [];
  patientConsultations: Consultation[] = [];
  loading = false;
  showConsultationForm = false;
  selectedRdv: RendezVous | null = null;
  consultationForm: FormGroup;
  currentUserId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private rdvService: RendezVousService,
    private authService: AuthService,
    private route: ActivatedRoute
  ) {
    this.consultationForm = this.fb.group({
      diagnostic: ['', Validators.required],
      ordonnance: [''],
      traitement: [''],
      notes: [''],
      prix: [0, [Validators.required, Validators.min(0)]]
    });
  }

  ngOnInit(): void {
    this.currentUserId = this.authService.getCurrentUserId();
    this.loadPatients();
    
    // Vérifier si on arrive d'un RDV pour consultation
    this.route.queryParams.subscribe(params => {
      if (params['patientId'] && params['rdvId'] && params['action'] === 'consultation') {
        this.startConsultation(parseInt(params['rdvId']));
      }
    });
  }

  loadPatients(): void {
    if (!this.currentUserId) return;
    
    this.loading = true;
    this.rdvService.getRendezVousByMedecin(this.currentUserId).subscribe({
      next: (rdvs: RendezVous[]) => {
        // Extraire les patients uniques
        const patientMap = new Map();
        rdvs.forEach(rdv => {
          if (!patientMap.has(rdv.patientId)) {
            patientMap.set(rdv.patientId, {
              id: rdv.patientId,
              nom: rdv.patientNom,
              prenom: rdv.patientPrenom,
              email: rdv.patientEmail,
              tel: rdv.patientTel
            });
          }
        });
        this.patients = Array.from(patientMap.values());
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  selectPatient(patient: any): void {
    this.selectedPatient = patient;
    this.loadPatientHistory(patient.id);
  }

  loadPatientHistory(patientId: number): void {
    // Charger les RDV du patient
    this.rdvService.getRendezVousByPatient(patientId).subscribe({
      next: (rdvs: RendezVous[]) => {
        this.patientRendezVous = rdvs.filter(rdv => rdv.medecinId === this.currentUserId)
                                      .sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime());
      }
    });
  }

  startConsultation(rdvId: number): void {
    const rdv = this.patientRendezVous.find(r => r.id === rdvId);
    if (rdv) {
      this.selectedRdv = rdv;
      this.showConsultationForm = true;
    }
  }

  submitConsultation(): void {
    if (this.consultationForm.invalid || !this.selectedRdv) return;
    
    const consultationData = {
      diagnostic: this.consultationForm.value.diagnostic,
      ordonnance: this.consultationForm.value.ordonnance,
      traitement: this.consultationForm.value.traitement,
      notes: this.consultationForm.value.notes,
      prix: this.consultationForm.value.prix
    };
    
    this.rdvService.createConsultation(this.selectedRdv.id, consultationData).subscribe({
      next: (consultation: Consultation) => {
        // Mettre à jour le statut du RDV
        this.rdvService.updateStatus(this.selectedRdv!.id, 'TERMINE').subscribe();
        
        // Réinitialiser le formulaire
        this.consultationForm.reset();
        this.showConsultationForm = false;
        this.selectedRdv = null;
        
        // Recharger l'historique
        this.loadPatientHistory(this.selectedPatient.id);
        
        alert('Consultation enregistrée avec succès !');
      },
      error: (err) => {
        console.error('Erreur création consultation:', err);
        alert('Erreur lors de l\'enregistrement de la consultation');
      }
    });
  }

  cancelConsultation(): void {
    this.showConsultationForm = false;
    this.selectedRdv = null;
    this.consultationForm.reset();
  }

  getRdvStatusClass(statut: string): string {
    switch (statut) {
      case 'CONFIRME': return 'text-success';
      case 'EN_ATTENTE': return 'text-warning';
      case 'ANNULE': return 'text-danger';
      case 'TERMINE': return 'text-secondary';
      default: return 'text-info';
    }
  }

  canAddConsultation(rdv: RendezVous): boolean {
    return rdv.statut === 'CONFIRME';
  }
}