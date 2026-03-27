import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'welcome', pathMatch: 'full' },
  
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

  // 🔐 Route protégée ADMIN pour créer médecin
  {
    path: 'admin/create-medecin',
    canActivate: [adminGuard],
    loadComponent: () => import('./views/admin/create-medecin/create-medecin.component')
      .then(m => m.CreateMedecinComponent)
  },

  {
    path: 'admin',
    canActivate: [adminGuard],
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('./views/admin/dashboard/dashboard.component')
          .then(m => m.DashboardComponent)
      }
    ]
  },

  {
    path: 'medecin',
    canActivate: [authGuard],
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('./views/medecin/dashboard/dashboard.component')
          .then(m => m.DashboardComponent)
      }
    ]
  },

  {
    path: 'patient',
    canActivate: [authGuard],
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('./views/patient/dashboard/dashboard.component')
          .then(m => m.DashboardComponent)
      }
    ]
  },

  { path: '**', redirectTo: 'welcome' }
];