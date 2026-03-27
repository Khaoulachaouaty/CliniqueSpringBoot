import { Component } from '@angular/core';
import { RouterOutlet, Router, NavigationEnd } from '@angular/router';
import { CommonModule } from '@angular/common';
import { filter } from 'rxjs/operators';
import { NavbarComponent } from './views/shared/navbar/navbar.component';
import { FooterComponent } from './views/shared/footer/footer.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule, NavbarComponent, FooterComponent],
  template: `
    <div class="d-flex flex-column min-vh-100">
      <!-- Navbar visible partout SAUF login/register -->
      @if (showNavbar) {
        <app-navbar />
      }
      
      <main class="flex-fill">
        <router-outlet />
      </main>
      
      <!-- Footer visible partout SAUF login/register -->
      @if (showFooter) {
        <app-footer />
      }
    </div>
  `,
  styles: [`:host { display: block; }`]
})
export class AppComponent {
  showNavbar = true;
  showFooter = true;

  // Routes SANS navbar (login, register)
  private hiddenRoutes = ['/login', '/register'];

  constructor(private router: Router) {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      const url = event.urlAfterRedirects;
      // Cacher navbar UNIQUEMENT sur login et register
      // PAS sur admin (le dashboard admin doit avoir la navbar)
      this.showNavbar = !this.hiddenRoutes.some(route => url === route);
      this.showFooter = this.showNavbar;
    });
  }
}