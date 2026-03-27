import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive, Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  isMenuOpen = false;
  isDropdownOpen = false;

  constructor(public authService: AuthService, private router: Router) {}

  get dashboardLink(): string {
    const role = this.authService.userRole();
    switch(role) {
      case 'ADMIN': return '/admin/dashboard';
      case 'MEDECIN': return '/medecin/dashboard';
      case 'PATIENT': return '/patient/dashboard';
      default: return '/';
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}