// src/app/views/medecin/rendezvous-list/rendezvous-list.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms'; // ✅ AJOUTÉ
import { RendezVousService } from '../../../services/rendezvous.service';
import { AuthService } from '../../../services/auth.service';
import { RendezVous } from '../../../models/rendezvous.model';
import { MedecinNavbarComponent } from '../medecin-navbar/medecin-navbar.component';

@Component({
  selector: 'app-rendezvous-list',
  standalone: true,
  imports: [CommonModule, FormsModule, MedecinNavbarComponent], // ✅ AJOUTÉ FormsModule
  template: `
    
    <div class="container-fluid py-4">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="text-primary">
          <i class="bi bi-calendar-check me-2"></i>Mes Rendez-vous
        </h2>
        
        <div class="d-flex gap-2">
          <select class="form-select" [(ngModel)]="filterStatut" (change)="applyFilter()">
            <option value="">Tous les statuts</option>
            <option value="EN_ATTENTE">En attente</option>
            <option value="CONFIRME">Confirmé</option>
            <option value="TERMINE">Terminé</option>
            <option value="ANNULE">Annulé</option>
            <option value="NON_VENU">Non venu</option>
          </select>
        </div>
      </div>

      <div class="card shadow-sm">
        <div class="card-body p-0">
          <div *ngIf="loading" class="text-center py-4">
            <div class="spinner-border text-primary"></div>
          </div>
          
          <div *ngIf="!loading && filteredRendezVous.length === 0" class="text-center py-4 text-muted">
            <i class="bi bi-calendar-x fs-1"></i>
            <p class="mt-2">Aucun rendez-vous trouvé</p>
          </div>

          <div class="table-responsive" *ngIf="!loading && filteredRendezVous.length > 0">
            <table class="table table-hover mb-0">
              <thead class="table-light">
                <tr>
                  <th>Date & Heure</th>
                  <th>Patient</th>
                  <th>Motif</th>
                  <th>Statut</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                <tr *ngFor="let rdv of filteredRendezVous" [class.table-warning]="isToday(rdv.date) && rdv.statut === 'CONFIRME'">
                  <td>
                    <div class="fw-bold">{{ rdv.date | date:'dd/MM/yyyy' }}</div>
                    <small class="text-muted">{{ rdv.heure }}</small>
                    <span *ngIf="isToday(rdv.date)" class="badge bg-info ms-2">Aujourd'hui</span>
                  </td>
                  <td>
                    <div class="d-flex align-items-center gap-2">
                      <div class="rounded-circle bg-primary text-white d-flex align-items-center justify-content-center"
                           style="width: 35px; height: 35px;">
                        {{ rdv.patientPrenom.charAt(0) }}{{ rdv.patientNom.charAt(0) }}
                      </div>
                      <div>
                        <div class="fw-bold">{{ rdv.patientPrenom }} {{ rdv.patientNom }}</div>
                        <small class="text-muted">{{ rdv.patientTel }}</small>
                      </div>
                    </div>
                  </td>
                  <td>{{ rdv.motif }}</td>
                  <td>
                    <span class="badge" [class]="'bg-' + getStatusClass(rdv.statut)">
                      {{ rdv.statut }}
                    </span>
                  </td>
                  <td>
                    <div class="btn-group btn-group-sm">
                      <button *ngIf="rdv.statut === 'EN_ATTENTE'" 
                              class="btn btn-success" 
                              (click)="confirmRdv(rdv)"
                              title="Confirmer">
                        <i class="bi bi-check-lg"></i>
                      </button>
                      
                      <button *ngIf="rdv.statut === 'CONFIRME' && isPast(rdv)" 
                              class="btn btn-primary" 
                              (click)="markAsDone(rdv)"
                              title="Patient venu - Terminer">
                        <i class="bi bi-check-all"></i> Terminer
                      </button>
                      
                      <button *ngIf="rdv.statut === 'CONFIRME' && isPast(rdv)" 
                              class="btn btn-outline-dark" 
                              (click)="markAsNoShow(rdv)"
                              title="Non venu">
                        <i class="bi bi-person-x"></i>
                      </button>
                      
                      <button *ngIf="rdv.statut === 'TERMINE'" 
                              class="btn btn-outline-primary" 
                              (click)="viewConsultation(rdv)"
                              title="Voir consultation">
                        <i class="bi bi-file-medical"></i>
                      </button>
                      
                      <button class="btn btn-outline-danger" 
                              (click)="cancelRdv(rdv)"
                              *ngIf="rdv.statut !== 'TERMINE' && rdv.statut !== 'ANNULE' && rdv.statut !== 'NON_VENU'"
                              title="Annuler">
                        <i class="bi bi-x-lg"></i>
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .table-warning {
      background-color: #fff3cd !important;
    }
  `]
})
export class RendezvousListComponent implements OnInit {
  rendezVous: RendezVous[] = [];
  filteredRendezVous: RendezVous[] = [];
  loading = false;
  currentUserId: number | null = null;
  filterStatut: string = '';
  today = new Date();

  constructor(
    private rdvService: RendezVousService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.currentUserId = this.authService.getMedecinId();
    this.loadRendezVous();
  }

  loadRendezVous(): void {
    if (!this.currentUserId) return;
    
    this.loading = true;
    this.rdvService.getRendezVousByMedecin(this.currentUserId).subscribe({
      next: (rdvs) => {
        this.rendezVous = rdvs.sort((a, b) => {
          const dateA = new Date(a.date + 'T' + a.heure);
          const dateB = new Date(b.date + 'T' + b.heure);
          return dateB.getTime() - dateA.getTime();
        });
        this.applyFilter();
        this.loading = false;
      },
      error: (err) => {
        console.error('Erreur chargement RDV:', err);
        this.loading = false;
      }
    });
  }

  applyFilter(): void {
    if (this.filterStatut) {
      this.filteredRendezVous = this.rendezVous.filter(r => r.statut === this.filterStatut);
    } else {
      this.filteredRendezVous = this.rendezVous;
    }
  }

  confirmRdv(rdv: RendezVous): void {
    if (!this.currentUserId) return;
    
    this.rdvService.updateStatus(rdv.id, 'CONFIRME', this.currentUserId).subscribe({
      next: (updated) => {
        rdv.statut = updated.statut;
        this.applyFilter();
      },
      error: (err) => console.error('Erreur confirmation:', err)
    });
  }

  markAsDone(rdv: RendezVous): void {
    if (!this.currentUserId) return;
    
    if (confirm(`Confirmer que le patient est venu et terminer ce rendez-vous ?`)) {
      this.rdvService.updateStatus(rdv.id, 'TERMINE', this.currentUserId).subscribe({
        next: (updated) => {
          rdv.statut = updated.statut;
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
    
    if (confirm(`Marquer ce rendez-vous comme "Non venu" ?`)) {
      this.rdvService.updateStatus(rdv.id, 'NON_VENU', this.currentUserId).subscribe({
        next: (updated) => {
          rdv.statut = updated.statut;
          this.applyFilter();
        },
        error: (err) => console.error('Erreur:', err)
      });
    }
  }

  cancelRdv(rdv: RendezVous): void {
    if (!this.currentUserId) return;
    
    if (confirm(`Annuler ce rendez-vous ? Le patient sera notifié.`)) {
      this.rdvService.updateStatus(rdv.id, 'ANNULE', this.currentUserId).subscribe({
        next: (updated) => {
          rdv.statut = updated.statut;
          this.applyFilter();
        },
        error: (err) => console.error('Erreur annulation:', err)
      });
    }
  }

  viewConsultation(rdv: RendezVous): void {
    this.router.navigate(['/medecin/dossiers'], {
      queryParams: {
        patientId: rdv.patientId,
        rdvId: rdv.id,
        action: 'view'
      }
    });
  }

  isToday(dateStr: string): boolean {
    const rdvDate = new Date(dateStr);
    return rdvDate.toDateString() === this.today.toDateString();
  }

  isPast(rdv: RendezVous): boolean {
    const rdvDateTime = new Date(rdv.date + 'T' + rdv.heure);
    return rdvDateTime < this.today;
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
}