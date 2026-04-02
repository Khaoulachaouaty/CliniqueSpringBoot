import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { RendezVousService } from '../../../services/rendezvous.service';
import { RendezVous } from '../../../models/rendezvous.model';

@Component({
  selector: 'app-medecin-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  
  todayRdv: RendezVous[] = [];
  totalPatients = 0;
  pendingRdv = 0;
  loading = true;
  currentUserId: number | null = null;

  constructor(
    public authService: AuthService,
    private rdvService: RendezVousService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.currentUserId = this.authService.getCurrentUserId();
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    if (!this.currentUserId) return;
    
    const today = new Date().toISOString().split('T')[0];
    
    // RDV du jour
    this.rdvService.getRendezVousDuJour(this.currentUserId, today).subscribe({
      next: (rdvs: RendezVous[]) => {
        this.todayRdv = rdvs.filter(r => r.statut !== 'ANNULE');
        this.loading = false;
      },
      error: () => this.loading = false
    });
    
    // Tous les RDV pour stats
    this.rdvService.getRendezVousByMedecin(this.currentUserId).subscribe({
      next: (rdvs: RendezVous[]) => {
        // Patients uniques
        const patientIds = new Set(rdvs.map(r => r.patientId));
        this.totalPatients = patientIds.size;
        
        // RDV en attente
        this.pendingRdv = rdvs.filter(r => r.statut === 'EN_ATTENTE').length;
      }
    });
  }

  navigateTo(path: string): void {
    this.router.navigate([path]);
  }

  getStatusClass(statut: string): string {
    switch (statut) {
      case 'CONFIRME': return 'bg-success';
      case 'EN_ATTENTE': return 'bg-warning';
      case 'ANNULE': return 'bg-danger';
      case 'TERMINE': return 'bg-secondary';
      default: return 'bg-info';
    }
  }

  getStatusLabel(statut: string): string {
    switch (statut) {
      case 'CONFIRME': return 'Confirmé';
      case 'EN_ATTENTE': return 'En attente';
      case 'ANNULE': return 'Annulé';
      case 'TERMINE': return 'Terminé';
      default: return statut;
    }
  }
}