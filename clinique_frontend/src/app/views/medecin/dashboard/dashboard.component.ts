// src/app/views/medecin/dashboard/dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { RendezVousService } from '../../../services/rendezvous.service';
import { AuthService } from '../../../services/auth.service';
import { NotificationService } from '../../../services/notification.service';
import { RendezVous, Consultation, CalendarEvent } from '../../../models/rendezvous.model';
import { MedecinNavbarComponent } from '../medecin-navbar/medecin-navbar.component';

@Component({
  selector: 'app-medecin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, MedecinNavbarComponent],
  template: `
    
    <div class="container-fluid py-4">
      <!-- Header -->
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="text-primary">
          <i class="bi bi-speedometer2 me-2"></i>Tableau de Bord
        </h2>
        <span class="text-muted">{{ today | date:'fullDate' }}</span>
      </div>

      <!-- Stats Cards -->
      <div class="row g-4 mb-4">
        <div class="col-md-3">
          <div class="card stat-card bg-primary text-white" (click)="navigateTo('/medecin/rendezvous')">
            <div class="card-body">
              <div class="d-flex justify-content-between">
                <div>
                  <h6 class="card-title">RDV Aujourd'hui</h6>
                  <h3 class="mb-0">{{ stats.rdvToday }}</h3>
                </div>
                <i class="bi bi-calendar-check fs-1 opacity-50"></i>
              </div>
            </div>
          </div>
        </div>
        
        <div class="col-md-3">
          <div class="card stat-card bg-warning text-dark" (click)="navigateTo('/medecin/rendezvous')">
            <div class="card-body">
              <div class="d-flex justify-content-between">
                <div>
                  <h6 class="card-title">En Attente</h6>
                  <h3 class="mb-0">{{ stats.rdvPending }}</h3>
                </div>
                <i class="bi bi-hourglass-split fs-1 opacity-50"></i>
              </div>
            </div>
          </div>
        </div>
        
        <div class="col-md-3">
          <div class="card stat-card bg-success text-white" (click)="navigateTo('/medecin/patients')">
            <div class="card-body">
              <div class="d-flex justify-content-between">
                <div>
                  <h6 class="card-title">Mes Patients</h6>
                  <h3 class="mb-0">{{ stats.totalPatients }}</h3>
                </div>
                <i class="bi bi-people fs-1 opacity-50"></i>
              </div>
            </div>
          </div>
        </div>
        
        <div class="col-md-3">
          <div class="card stat-card bg-info text-white" (click)="navigateTo('/medecin/dossiers')">
            <div class="card-body">
              <div class="d-flex justify-content-between">
                <div>
                  <h6 class="card-title">Consultations</h6>
                  <h3 class="mb-0">{{ stats.totalConsultations }}</h3>
                </div>
                <i class="bi bi-clipboard-pulse fs-1 opacity-50"></i>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="row">
        <!-- RDV du jour -->
        <div class="col-lg-8">
          <div class="card shadow-sm">
            <div class="card-header bg-white d-flex justify-content-between align-items-center">
              <h5 class="mb-0">
                <i class="bi bi-calendar-day me-2 text-primary"></i>Rendez-vous du Jour
              </h5>
              <button class="btn btn-sm btn-primary" (click)="navigateTo('/medecin/rendezvous')">
                Voir tout <i class="bi bi-arrow-right ms-1"></i>
              </button>
            </div>
            <div class="card-body p-0">
              <div *ngIf="loading" class="text-center py-4">
                <div class="spinner-border text-primary"></div>
              </div>
              
              <div *ngIf="!loading && todayRendezVous.length === 0" class="text-center py-4 text-muted">
                <i class="bi bi-calendar-x fs-1"></i>
                <p class="mt-2">Aucun rendez-vous aujourd'hui</p>
              </div>

              <div class="list-group list-group-flush" *ngIf="!loading && todayRendezVous.length > 0">
                <div *ngFor="let rdv of todayRendezVous" class="list-group-item list-group-item-action"
                     [class.border-start-4]="true"
                     [style.border-left]="'4px solid ' + getStatusColor(rdv.statut)">
                  <div class="d-flex justify-content-between align-items-center">
                    <div>
                      <div class="d-flex align-items-center gap-2 mb-1">
                        <span class="badge" [class]="'bg-' + getStatusClass(rdv.statut)">
                          {{ rdv.statut }}
                        </span>
                        <span class="fw-bold">{{ rdv.heure }}</span>
                      </div>
                      <h6 class="mb-1">{{ rdv.patientPrenom }} {{ rdv.patientNom }}</h6>
                      <small class="text-muted">{{ rdv.motif }}</small>
                    </div>
                    
                    <div class="btn-group" *ngIf="rdv.statut === 'CONFIRME'">
                      <button class="btn btn-sm btn-success" (click)="markAsDone(rdv)" title="Patient venu - Terminer">
                        <i class="bi bi-check-lg"></i> Venu
                      </button>
                      <button class="btn btn-sm btn-outline-danger" (click)="markAsNoShow(rdv)" title="Patient non venu">
                        <i class="bi bi-x-lg"></i> Absent
                      </button>
                    </div>
                    
                    <button *ngIf="rdv.statut === 'EN_ATTENTE'" class="btn btn-sm btn-primary" (click)="confirmRdv(rdv)">
                      <i class="bi bi-check-circle"></i> Confirmer
                    </button>
                    
                    <span *ngIf="rdv.statut === 'TERMINE'" class="badge bg-success">
                      <i class="bi bi-check-all"></i> Terminé
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Actions rapides -->
        <div class="col-lg-4">
          <div class="card shadow-sm mb-4">
            <div class="card-header bg-white">
              <h5 class="mb-0">
                <i class="bi bi-lightning me-2 text-warning"></i>Actions Rapides
              </h5>
            </div>
            <div class="card-body">
              <div class="d-grid gap-2">
                <button class="btn btn-outline-primary" (click)="navigateTo('/medecin/calendrier')">
                  <i class="bi bi-calendar3 me-2"></i>Voir le Calendrier
                </button>
                <button class="btn btn-outline-success" (click)="navigateTo('/medecin/dossiers')">
                  <i class="bi bi-folder-medical me-2"></i>Dossiers Médicaux
                </button>
                <button class="btn btn-outline-info" (click)="navigateTo('/medecin/patients')">
                  <i class="bi bi-people me-2"></i>Liste des Patients
                </button>
              </div>
            </div>
          </div>

          <!-- Notifications récentes -->
          <div class="card shadow-sm">
            <div class="card-header bg-white d-flex justify-content-between align-items-center">
              <h5 class="mb-0">
                <i class="bi bi-bell me-2 text-warning"></i>Notifications Récentes
              </h5>
              <button class="btn btn-sm btn-link" (click)="navigateTo('/medecin/notifications')">
                Voir tout
              </button>
            </div>
            <div class="card-body p-0">
              <div *ngIf="recentNotifications.length === 0" class="text-center py-3 text-muted">
                <small>Aucune notification récente</small>
              </div>
              <div class="list-group list-group-flush">
                <div *ngFor="let notif of recentNotifications" class="list-group-item py-2">
                  <small class="text-muted">{{ notif.dateEnvoi | date:'HH:mm' }}</small>
                  <p class="mb-0 small">{{ notif.message }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .stat-card {
      cursor: pointer;
      transition: all 0.3s ease;
      border: none;
    }
    .stat-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 10px 25px rgba(0,0,0,0.15);
    }
    .border-start-4 {
      border-left-width: 4px !important;
    }
  `]
})
export class DashboardComponent implements OnInit {
  today = new Date();
  loading = false;
  todayRendezVous: RendezVous[] = [];
  recentNotifications: any[] = [];
  currentUserId: number | null = null;
  
  stats = {
    rdvToday: 0,
    rdvPending: 0,
    totalPatients: 0,
    totalConsultations: 0
  };

  constructor(
    private rdvService: RendezVousService,
    private authService: AuthService,
    private notificationService: NotificationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.currentUserId = this.authService.getMedecinId();
    if (this.currentUserId) {
      this.loadTodayRendezVous();
      this.loadStats();
      this.loadRecentNotifications();
    }
  }

  loadTodayRendezVous(): void {
    if (!this.currentUserId) return;
    
    this.loading = true;
    const todayStr = this.formatDate(this.today);
    
    this.rdvService.getRendezVousDuJour(this.currentUserId, todayStr).subscribe({
      next: (rdvs) => {
        this.todayRendezVous = rdvs.sort((a, b) => a.heure.localeCompare(b.heure));
        this.loading = false;
      },
      error: (err) => {
        console.error('Erreur chargement RDV:', err);
        this.loading = false;
      }
    });
  }

  loadStats(): void {
    if (!this.currentUserId) return;
    
    // RDV en attente
    this.rdvService.getRendezVousByMedecin(this.currentUserId).subscribe({
      next: (rdvs) => {
        this.stats.rdvPending = rdvs.filter(r => r.statut === 'EN_ATTENTE').length;
        this.stats.rdvToday = this.todayRendezVous.length;
        
        // Patients uniques
        const uniquePatients = new Set(rdvs.map(r => r.patientId));
        this.stats.totalPatients = uniquePatients.size;
      }
    });

    // Consultations
    this.rdvService.getConsultationsByMedecin(this.currentUserId).subscribe({
      next: (consultations) => {
        this.stats.totalConsultations = consultations.length;
      }
    });
  }

  loadRecentNotifications(): void {
    if (!this.currentUserId) return;
    
    this.notificationService.getNotificationsByMedecin(this.currentUserId).subscribe({
      next: (notifs) => {
        this.recentNotifications = notifs.slice(0, 3);
      }
    });
  }

  confirmRdv(rdv: RendezVous): void {
    if (!this.currentUserId) return;
    
    this.rdvService.updateStatus(rdv.id, 'CONFIRME', this.currentUserId).subscribe({
      next: (updated) => {
        rdv.statut = updated.statut;
        this.loadStats();
      },
      error: (err) => console.error('Erreur confirmation:', err)
    });
  }

  markAsDone(rdv: RendezVous): void {
    if (!this.currentUserId) return;
    
    if (confirm(`Confirmer que ${rdv.patientPrenom} ${rdv.patientNom} est venu et terminer le rendez-vous ?`)) {
      this.rdvService.updateStatus(rdv.id, 'TERMINE', this.currentUserId).subscribe({
        next: (updated) => {
          rdv.statut = updated.statut;
          // Redirection vers la consultation/facture
          this.router.navigate(['/medecin/dossiers'], {
            queryParams: {
              patientId: rdv.patientId,
              rdvId: rdv.id,
              action: 'consultation'
            }
          });
        },
        error: (err) => console.error('Erreur:', err)
      });
    }
  }

  markAsNoShow(rdv: RendezVous): void {
    if (!this.currentUserId) return;
    
    if (confirm(`Marquer ${rdv.patientPrenom} ${rdv.patientNom} comme "Non venu" ?`)) {
      this.rdvService.updateStatus(rdv.id, 'NON_VENU', this.currentUserId).subscribe({
        next: (updated) => {
          rdv.statut = updated.statut;
        },
        error: (err) => console.error('Erreur:', err)
      });
    }
  }

  navigateTo(path: string): void {
    this.router.navigate([path]);
  }

  getStatusClass(statut: string): string {
    switch (statut) {
      case 'CONFIRME': return 'success';
      case 'EN_ATTENTE': return 'warning';
      case 'ANNULE': return 'danger';
      case 'TERMINE': return 'secondary';
      case 'NON_VENU': return 'dark';
      default: return 'info';
    }
  }

  getStatusColor(statut: string): string {
    switch (statut) {
      case 'CONFIRME': return '#198754';
      case 'EN_ATTENTE': return '#ffc107';
      case 'ANNULE': return '#dc3545';
      case 'TERMINE': return '#6c757d';
      case 'NON_VENU': return '#212529';
      default: return '#0dcaf0';
    }
  }

  private formatDate(date: Date): string {
    return date.toISOString().split('T')[0];
  }
}