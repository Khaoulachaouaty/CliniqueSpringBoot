import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';
import { medecinGuard } from './guards/medecin.guard';
import { patientGuard } from './guards/patient.guard';
import { MedecinListComponent } from './views/admin/medecin-list/medecin-list.component';
import { PatientListComponent } from './views/admin/patient-list/patient-list.component';

export const routes: Routes = [
  { path: '', redirectTo: 'welcome', pathMatch: 'full' },
  
  // ========== PUBLIC ==========
  {
    path: 'welcome',
    loadComponent: () => import('./views/shared/landing/landing.component')
      .then(m => m.LandingComponent)
  },
  
  {
    path: 'login',
    loadComponent: () => import('./views/auth/login/login.component')
      .then(m => m.LoginComponent)
  },
  
  {
    path: 'register',
    loadComponent: () => import('./views/auth/register-patient/register-patient.component')
      .then(m => m.RegisterPatientComponent)
  },

  // ========== ADMIN ==========
  {
    path: 'admin',
    canActivate: [adminGuard],
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('./views/admin/dashboard/dashboard.component')
          .then(m => m.DashboardComponent)
      },
      {
        path: 'medecins',
        component: MedecinListComponent
      },
      {
        path: 'patients',
        component: PatientListComponent
      },
      {
        path: 'create-medecin',
        loadComponent: () => import('./views/admin/create-medecin/create-medecin.component')
          .then(m => m.CreateMedecinComponent)
      },
      {
        path: 'rendezvous',
        loadComponent: () => import('./views/admin/rendezvous-list/rendezvous-list.component')
          .then(m => m.GestionRendezvousComponent)
      }
    ]
  },

  // ========== MEDECIN ==========
  {
    path: 'medecin',
    canActivate: [medecinGuard],
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('./views/medecin/dashboard/dashboard.component')
          .then(m => m.DashboardComponent)
      },
      {
        path: 'rendezvous',
        loadComponent: () => import('./views/medecin/calendrier-rdv/calendrier-rdv.component')
          .then(m => m.CalendrierRdvComponent)
      },
      // 🔥 SUPPRIMÉ : disponibilites (plus d'entité Disponibilite)
      {
        path: 'patients',
        loadComponent: () => import('./views/medecin/mes-patients/mes-patients.component')
          .then(m => m.MesPatientsComponent)
      },
      {
        path: 'dossiers',
        loadComponent: () => import('./views/medecin/dossiers-medicaux/dossiers-medicaux.component')
          .then(m => m.DossiersMedicauxComponent)
      },
      {
        path: 'notifications',
        loadComponent: () => import('./views/medecin/notification-medecin/notification-medecin.component')
          .then(m => m.NotificationMedecinComponent)
      }
    ]
  },

  // ========== PATIENT ==========
  {
    path: 'patient',
    canActivate: [patientGuard],
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('./views/patient/dashboard/dashboard.component')
          .then(m => m.DashboardComponent)
      },
      {
        path: 'rendezvous',
        loadComponent: () => import('./views/patient/mes-rendezvous/mes-rendezvous.component')
          .then(m => m.MesRendezvousComponent)
      },
      {
        path: 'prendre-rdv',
        loadComponent: () => import('./views/patient/prendre-rdv/prendre-rdv.component')
          .then(m => m.PrendreRdvComponent)
      },
      {
        path: 'dossier',
        loadComponent: () => import('./views/patient/mon-dossier/mon-dossier.component')
          .then(m => m.MonDossierComponent)
      },
      {
        path: 'medecins',
        loadComponent: () => import('./views/patient/liste-medecins/liste-medecins.component')
          .then(m => m.ListeMedecinsComponent)
      },
      {
        path: 'notifications',
        loadComponent: () => import('./views/patient/notifications/notifications.component')
          .then(m => m.NotificationsComponent)
      }
    ]
  },

  // ========== FALLBACK ==========
  { path: '**', redirectTo: 'welcome' }
];