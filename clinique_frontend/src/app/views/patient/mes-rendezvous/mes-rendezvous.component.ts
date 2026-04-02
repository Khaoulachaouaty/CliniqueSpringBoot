// views/patient/mes-rendezvous/mes-rendezvous.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { RendezVous } from '../../../models/rendezvous.model';
import { RendezVousService } from '../../../services/rendezvous.service';

@Component({
  selector: 'app-mes-rendezvous',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './mes-rendezvous.component.html',
  styleUrls: ['./mes-rendezvous.component.css']
})
export class MesRendezvousComponent implements OnInit {
  rendezVous: RendezVous[] = [];
  loading = true;
  errorMessage = '';

  constructor(
    private rdvService: RendezVousService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadRendezVous();
  }

  loadRendezVous(): void {
    const user = this.authService.currentUser();
    console.log('🔍 currentUser:', user);

    if (!user?.userId) {
      this.errorMessage = 'Utilisateur non connecté';
      this.loading = false;
      return;
    }

    console.log('📤 Appel API avec userId:', user.userId);

    this.rdvService.getRendezVousByPatient(user.userId).subscribe({
      next: (data: RendezVous[]) => {
        console.log('📥 Réponse API:', data);
        this.rendezVous = data.sort((a: RendezVous, b: RendezVous) => 
          new Date(b.date).getTime() - new Date(a.date).getTime()
        );
        this.loading = false;
      },
      error: (err: any) => {
        console.error('❌ Erreur API:', err);
        this.errorMessage = 'Erreur lors du chargement des rendez-vous';
        this.loading = false;
      }
    });
  }

  getStatusColor(statut: string): string {
    const colors: { [key: string]: string } = {
      'EN_ATTENTE': '#ffc107',
      'CONFIRME': '#28a745',
      'ANNULE': '#dc3545',
      'TERMINE': '#6c757d'
    };
    return colors[statut] || '#6c757d';
  }

  getStatusBadgeClass(statut: string): string {
    const classes: { [key: string]: string } = {
      'EN_ATTENTE': 'bg-warning text-dark',
      'CONFIRME': 'bg-success',
      'ANNULE': 'bg-danger',
      'TERMINE': 'bg-secondary'
    };
    return classes[statut] || 'bg-secondary';
  }

  getStatusLabel(statut: string): string {
    const labels: { [key: string]: string } = {
      'EN_ATTENTE': 'En attente',
      'CONFIRME': 'Confirmé',
      'ANNULE': 'Annulé',
      'TERMINE': 'Terminé'
    };
    return labels[statut] || statut;
  }

  annulerRdv(id: number): void {
    if (!confirm('Êtes-vous sûr de vouloir annuler ce rendez-vous ?')) return;
    
    const patientId = this.authService.currentUser()?.userId;
    if (!patientId) return;

    this.rdvService.cancelRendezVous(id, patientId).subscribe({
      next: () => {
        console.log('✅ RDV annulé');
        this.loadRendezVous();
      },
      error: (err: any) => {
        console.error('❌ Erreur annulation:', err);
        alert('Erreur lors de l\'annulation');
      }
    });
  }
  
}