import { Component, computed, signal, OnInit, OnDestroy, effect } from '@angular/core';
import { RouterOutlet, Router, NavigationEnd } from '@angular/router';
import { CommonModule } from '@angular/common';
import { filter } from 'rxjs/operators';
import { NavbarComponent } from './views/shared/navbar/navbar.component';
import { FooterComponent } from './views/shared/footer/footer.component';
import { AdminNavbarComponent } from './views/admin/admin-navbar/admin-navbar.component';
import { MedecinNavbarComponent } from './views/medecin/medecin-navbar/medecin-navbar.component';
import { AuthService } from './services/auth.service';
import { PatientNavbarComponent } from "./views/patient/patient-navbar/patient-navbar.component";
import { NotificationService } from './services/notification.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    CommonModule,
    NavbarComponent,
    AdminNavbarComponent,
    MedecinNavbarComponent,
    FooterComponent,
    PatientNavbarComponent
  ],
  template: `
    <div class="d-flex flex-column min-vh-100">
      <!-- Navbar publique - UNIQUEMENT si personne n'est connecté -->
      @if (showPublicNavbar()) {
        <app-navbar />
      }
      
      <!-- Navbar admin - si connecté en tant qu'ADMIN -->
      @if (showAdminNavbar()) {
        <app-admin-navbar />
      }
      
      <!-- Navbar médecin - si connecté en tant que MEDECIN -->
      @if (showMedecinNavbar()) {
        <app-medecin-navbar />
      }
      
      <!-- Navbar patient - si connecté en tant que PATIENT -->
      @if (showPatientNavbar()) {
        <app-patient-navbar />
      }
      
      <main class="flex-fill">
        <router-outlet />
      </main>
      
      @if (showFooter()) {
        <app-footer />
      }
    </div>
  `,
  styles: [`:host { display: block; }`]
})
export class AppComponent implements OnInit, OnDestroy {
  // Computed signals pour la réactivité
  showPublicNavbar = computed(() => {
    const role = this.authService.userRole();
    const isLoggedIn = this.authService.isLoggedIn();
    // Afficher navbar publique uniquement si NON connecté
    return !isLoggedIn || role === null;
  });
  
  showAdminNavbar = computed(() => {
    return this.authService.userRole() === 'ADMIN';
  });
  
  showMedecinNavbar = computed(() => {
    return this.authService.userRole() === 'MEDECIN';
  });
  
  showPatientNavbar = computed(() => {
    return this.authService.userRole() === 'PATIENT';
  });
  
  showFooter = computed(() => {
    const url = this.currentUrl();
    const noFooterRoutes = ['/login', '/register'];
    return !noFooterRoutes.includes(url);
  });
  
  currentUrl = signal('/');

  constructor(
    private router: Router,
    private authService: AuthService,
    private notificationService: NotificationService
  ) {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      this.currentUrl.set(event.urlAfterRedirects);
    });

    // 🔥 Utiliser 'effect' pour réagir aux changements du signal
    effect(() => {
      const isLoggedIn = this.authService.isLoggedIn();
      if (isLoggedIn) {
        this.initWebSocketIfLoggedIn();
      } else {
        this.notificationService.closeWebSocket();
      }
    });
  }

  ngOnInit(): void {
    // Initialiser WebSocket si utilisateur déjà connecté
    this.initWebSocketIfLoggedIn();
    
    // Demander la permission pour les notifications du navigateur
    this.notificationService.requestNotificationPermission();
  }

  ngOnDestroy(): void {
    // Fermer WebSocket quand l'app se ferme
    this.notificationService.closeWebSocket();
  }

  /**
   * Initialiser WebSocket si utilisateur connecté
   */
  private initWebSocketIfLoggedIn(): void {
    const isLoggedIn = this.authService.isLoggedIn();
    const role = this.authService.userRole();
    
    if (isLoggedIn && (role === 'PATIENT' || role === 'MEDECIN')) {
      console.log('🔌 Initialisation WebSocket pour:', role);
      this.notificationService.initWebSocket();
      
      // Rafraîchir les notifications au démarrage
      setTimeout(() => {
        this.notificationService.refreshNotifications();
      }, 1000);
    }
  }
}