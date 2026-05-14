import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../../../services/api.service';
import { Medecin } from '../../../models/medecin.model';

@Component({
  selector: 'app-medecin-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './medecin-list.component.html',
  styleUrls: ['./medecin-list.component.css']
})
export class MedecinListComponent implements OnInit {
  medecins: Medecin[] = [];
  filteredMedecins: Medecin[] = [];
  loading = true;
  searchTerm = '';
  selectedSpecialite = '';
  errorMessage = '';
  successMessage = '';

  // Modals
  showDeleteModal = false;
  showDetailsModal = false;
  showEditModal = false;

  medecinToDelete: Medecin | null = null;
  medecinToEdit: Medecin | null = null;
  selectedMedecin: Medecin | null = null;
  medecinDetails: any = null;
  loadingDetails = false;
  savingEdit = false;

  editForm = { nom: '', prenom: '', tel: '', specialite: '' };

  specialites: string[] = [];

  constructor(private apiService: ApiService, private router: Router) {}

  ngOnInit(): void {
    this.loadMedecins();
  }

  navigateTo(path: string): void {
    this.router.navigate([path]);
  }

  loadMedecins(): void {
    this.loading = true;
    this.apiService.getAllMedecins().subscribe({
      next: (data) => {
        this.medecins = data;
        this.filteredMedecins = data;
        this.extractSpecialites();
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Erreur lors du chargement.';
        this.loading = false;
      }
    });
  }

  extractSpecialites(): void {
    this.specialites = [...new Set(this.medecins.map(m => m.specialite))].sort();
  }

  applyFilters(): void {
    let result = this.medecins;
    if (this.searchTerm) {
      const term = this.searchTerm.toLowerCase();
      result = result.filter(m =>
        `${m.prenom} ${m.nom}`.toLowerCase().includes(term) ||
        m.specialite.toLowerCase().includes(term) ||
        m.email.toLowerCase().includes(term)
      );
    }
    if (this.selectedSpecialite) {
      result = result.filter(m => m.specialite === this.selectedSpecialite);
    }
    this.filteredMedecins = result;
  }

  onSearch(event: Event): void {
    this.searchTerm = (event.target as HTMLInputElement).value;
    this.applyFilters();
  }

  onSpecialiteChange(event: Event): void {
    this.selectedSpecialite = (event.target as HTMLSelectElement).value;
    this.applyFilters();
  }

  clearFilters(): void {
    this.searchTerm = '';
    this.selectedSpecialite = '';
    this.applyFilters();
  }

  // ── Détails ──
  openDetails(medecin: Medecin): void {
    this.selectedMedecin = medecin;
    this.showDetailsModal = true;
    this.loadingDetails = true;
    this.apiService.getMedecinDetails(medecin.id).subscribe({
      next: (details) => { this.medecinDetails = details; this.loadingDetails = false; },
      error: () => { this.medecinDetails = { nombrePatients: 0, rendezVousTotal: 0 }; this.loadingDetails = false; }
    });
  }

  closeDetails(): void {
    this.showDetailsModal = false;
    this.selectedMedecin = null;
    this.medecinDetails = null;
  }

  // ── Suppression ──
  openDeleteModal(medecin: Medecin): void {
    this.medecinToDelete = medecin;
    this.showDeleteModal = true;
  }

  closeDeleteModal(): void {
    this.showDeleteModal = false;
    this.medecinToDelete = null;
  }

  confirmDelete(): void {
    if (!this.medecinToDelete) return;
    this.apiService.deleteMedecin(this.medecinToDelete.id).subscribe({
      next: () => {
        this.successMessage = 'Médecin supprimé avec succès.';
        this.closeDeleteModal();
        this.loadMedecins();
        setTimeout(() => this.successMessage = '', 3000);
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erreur lors de la suppression.';
        this.closeDeleteModal();
        setTimeout(() => this.errorMessage = '', 4000);
      }
    });
  }

  // ── Modification ──
  openEditModal(medecin: Medecin): void {
    this.medecinToEdit = medecin;
    this.editForm = {
      nom: medecin.nom || '',
      prenom: medecin.prenom || '',
      tel: medecin.tel || '',
      specialite: medecin.specialite || ''
    };
    this.showEditModal = true;
  }

  closeEditModal(): void {
    this.showEditModal = false;
    this.medecinToEdit = null;
    this.savingEdit = false;
  }

  confirmEdit(): void {
    if (!this.medecinToEdit) return;
    this.savingEdit = true;
    this.apiService.updateMedecin(this.medecinToEdit.id, this.editForm).subscribe({
      next: () => {
        this.successMessage = 'Médecin modifié avec succès.';
        this.closeEditModal();
        this.loadMedecins();
        setTimeout(() => this.successMessage = '', 3000);
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erreur lors de la modification.';
        this.savingEdit = false;
        setTimeout(() => this.errorMessage = '', 4000);
      }
    });
  }

  formatPhone(tel?: string): string {
    if (!tel) return 'Non renseigné';
    if (tel.length === 8) return `${tel.slice(0,2)} ${tel.slice(2,4)} ${tel.slice(4,6)} ${tel.slice(6,8)}`;
    return tel;
  }

  getInitials(prenom: string, nom: string): string {
    return `${prenom?.charAt(0) || ''}${nom?.charAt(0) || ''}`.toUpperCase();
  }
}
