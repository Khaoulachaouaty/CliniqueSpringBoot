// views/patient/mon-dossier/mon-dossier.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { RendezVousService } from '../../../services/rendezvous.service';
import { Consultation, DossierMedicalResponse } from '../../../models/rendezvous.model';

@Component({
  selector: 'app-mon-dossier',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './mon-dossier.component.html',
  styleUrls: ['./mon-dossier.component.css']
})
export class MonDossierComponent implements OnInit {
  
  user: any = null;
  consultations: Consultation[] = [];
  dossierMedical: DossierMedicalResponse | null = null;
  dossierText: string = '';
  loading = false;
  updating = false;
  editingDossier = false;
  activeTab: string = 'consultations';

  constructor(
    private authService: AuthService,
    private rdvService: RendezVousService
  ) {}

  ngOnInit(): void {
    this.user = this.authService.currentUser();
    this.loadConsultations();
    this.loadDossierMedical();
  }

  loadConsultations(): void {
    const patientId = this.authService.getPatientId();
    if (!patientId) return;

    this.loading = true;
    this.rdvService.getConsultationsByPatient(patientId).subscribe({
      next: (consultations) => {
        this.consultations = consultations.sort((a, b) => 
          new Date(b.dateConsultation || 0).getTime() - new Date(a.dateConsultation || 0).getTime()
        );
        this.loading = false;
      },
      error: (err) => {
        console.error('Erreur chargement consultations:', err);
        this.loading = false;
      }
    });
  }

  loadDossierMedical(): void {
    const patientId = this.authService.getPatientId();
    const medecinId = this.authService.getCurrentUserId();
    if (!patientId || !medecinId) return;

    this.rdvService.consulterDossierMedical(patientId, medecinId).subscribe({
      next: (dossier) => {
        this.dossierMedical = dossier;
        this.dossierText = dossier?.dossierMedical || '';
      },
      error: (err) => {
        console.error('Erreur chargement dossier médical:', err);
      }
    });
  }

  saveDossierMedical(): void {
    const patientId = this.authService.getPatientId();
    const medecinId = this.authService.getCurrentUserId();
    if (!patientId || !medecinId) return;

    this.updating = true;
    this.rdvService.updateDossierMedical(patientId, medecinId, this.dossierText).subscribe({
      next: (dossier) => {
        this.dossierMedical = dossier;
        this.dossierText = dossier?.dossierMedical || '';
        this.editingDossier = false;
        this.updating = false;
        alert('Dossier médical mis à jour avec succès');
      },
      error: (err) => {
        console.error('Erreur mise à jour dossier:', err);
        this.updating = false;
        alert('Erreur lors de la mise à jour');
      }
    });
  }

  cancelEditDossier(): void {
    this.editingDossier = false;
    this.dossierText = this.dossierMedical?.dossierMedical || '';
  }

  payerFacture(consultation: Consultation): void {
    if (!confirm(`Confirmer le paiement de ${this.formatMontant(consultation.montantTotal || consultation.prixConsultation)} ?`)) return;
    
    this.rdvService.updateStatutPaiement(consultation.id, 'PAYE').subscribe({
      next: (facture) => {
        consultation.statutPaiement = 'PAYE';
        alert('Paiement effectué avec succès !');
        
        if (confirm('Télécharger la facture PDF ?')) {
          this.telechargerFacture(consultation.id);
        }
      },
      error: (err) => {
        console.error('Erreur paiement:', err);
        alert('Erreur lors du paiement');
      }
    });
  }

  telechargerFacture(consultationId: number): void {
    this.rdvService.genererFacturePDF(consultationId).subscribe({
      next: (facture) => {
        console.log('Facture PDF générée:', facture);
        alert(`Facture N° ${facture.numeroFacture} prête au téléchargement`);
      },
      error: (err) => {
        console.error('Erreur génération PDF:', err);
        alert('Erreur lors de la génération du PDF');
      }
    });
  }

  getInitials(prenom: string, nom: string): string {
    return (prenom?.charAt(0) || '') + (nom?.charAt(0) || '');
  }

  formatMontant(montant: number): string {
    return new Intl.NumberFormat('fr-TN', {
      style: 'currency',
      currency: 'TND'
    }).format(montant || 0);
  }
}