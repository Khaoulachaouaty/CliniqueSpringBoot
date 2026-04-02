// views/patient/mon-dossier/mon-dossier.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-mon-dossier',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container py-4">
      <h2 class="mb-4 text-success">
        <i class="bi bi-folder-medical me-2"></i>Mon Dossier Médical
      </h2>

      <div class="card border-0 shadow-sm">
        <div class="card-header bg-success text-white">
          <h5 class="mb-0">Informations personnelles</h5>
        </div>
        <div class="card-body">
          @if (user) {
            <div class="row g-3">
              <div class="col-md-6">
                <label class="text-muted small">Nom</label>
                <p class="fw-bold">{{ user.nom }}</p>
              </div>
              <div class="col-md-6">
                <label class="text-muted small">Prénom</label>
                <p class="fw-bold">{{ user.prenom }}</p>
              </div>
              <div class="col-md-6">
                <label class="text-muted small">Email</label>
                <p class="fw-bold">{{ user.email }}</p>
              </div>
              <div class="col-md-6">
                <label class="text-muted small">Téléphone</label>
                <p class="fw-bold">{{ user.tel || 'Non renseigné' }}</p>
              </div>
            </div>
          }
        </div>
      </div>

      <div class="alert alert-info mt-4">
        <i class="bi bi-info-circle me-2"></i>
        Les détails complets de votre dossier médical seront disponibles prochainement.
      </div>
    </div>
  `
})
export class MonDossierComponent implements OnInit {
  user: any = null;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.user = this.authService.currentUser();
  }
}