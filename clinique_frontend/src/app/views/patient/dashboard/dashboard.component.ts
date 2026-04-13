import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-patient-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container py-4">
      <h2 class="mb-4">
        <i class="bi bi-person-circle me-2"></i>Mon Espace Patient
      </h2>

      <div class="row g-4">
        <div class="col-md-4">
          <div class="card mb-3 border-0 shadow-sm">
            <div class="card-body text-center bg-primary text-white rounded">
              <i class="bi bi-person-circle display-4 mb-3 opacity-75"></i>
              <h5>Bienvenue {{ authService.currentUser()?.prenom }}</h5>
              <p class="mb-0 opacity-75">Gérez vos rendez-vous</p>
            </div>
          </div>
          
          <div class="list-group shadow-sm">
            <a href="#" class="list-group-item list-group-item-action active">
              <i class="bi bi-calendar-plus me-2"></i>Prendre RDV
            </a>
            <a href="#" class="list-group-item list-group-item-action">
              <i class="bi bi-calendar-list me-2"></i>Mes Rendez-vous
            </a>
            <a href="#" class="list-group-item list-group-item-action">
              <i class="bi bi-file-medical me-2"></i>Mon Dossier Médical
            </a>
            <a href="#" class="list-group-item list-group-item-action">
              <i class="bi bi-credit-card me-2"></i>Mes Factures
            </a>
          </div>
        </div>
        
        <div class="col-md-8">
          <div class="card shadow-sm mb-4">
            <div class="card-header bg-primary text-white">
              <h5 class="mb-0">Prochain rendez-vous</h5>
            </div>
            <div class="card-body">
              <div class="alert alert-info">
                <i class="bi bi-info-circle me-2"></i>
                Vous n'avez aucun rendez-vous prévu.
                <button class="btn btn-primary btn-sm ms-3">Prendre un RDV</button>
              </div>
            </div>
          </div>

          <div class="card shadow-sm">
            <div class="card-header bg-white">
              <h5 class="mb-0">Nos spécialités</h5>
            </div>
            <div class="card-body">
              <div class="row g-2">
                @for (spec of specialites; track spec) {
                  <div class="col-6 col-md-4">
                    <span class="badge bg-light text-dark w-100 p-2 border">
                      {{ spec }}
                    </span>
                  </div>
                }
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  specialites = ['Cardiologie', 'Dermatologie', 'Pédiatrie', 'Gynécologie', 
                 'Neurologie', 'Ophtalmologie', 'Orthopédie', 'Généraliste'];
  
  constructor(public authService: AuthService) {}
}