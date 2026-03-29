import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-medecin-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container py-4">
      <h2 class="mb-4">
        <i class="bi bi-heart-pulse me-2"></i>Espace Médecin
      </h2>

      <div class="row g-4">
        <div class="col-md-4">
          <div class="list-group shadow-sm">
            <a href="#" class="list-group-item list-group-item-action active">
              <i class="bi bi-calendar-check me-2"></i>Mes Rendez-vous
            </a>
            <a href="#" class="list-group-item list-group-item-action">
              <i class="bi bi-people me-2"></i>Mes Patients
            </a>
            <a href="#" class="list-group-item list-group-item-action">
              <i class="bi bi-clock me-2"></i>Mes Disponibilités
            </a>
            <a href="#" class="list-group-item list-group-item-action">
              <i class="bi bi-file-text me-2"></i>Dossiers Médicaux
            </a>
          </div>
        </div>
        
        <div class="col-md-8">
          <div class="card shadow-sm">
            <div class="card-header bg-white">
              <h5 class="mb-0">Rendez-vous du jour</h5>
            </div>
            <div class="card-body">
              <div class="alert alert-info d-flex align-items-center">
                <i class="bi bi-info-circle-fill me-2 fs-4"></i>
                <div>
                  <strong>Aucun rendez-vous prévu aujourd'hui.</strong><br>
                  <small>La gestion des rendez-vous sera implémentée prochainement.</small>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `
})
export class DashboardComponent {
  constructor(public authService: AuthService) {}
}