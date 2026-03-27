import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, catchError, of, tap } from 'rxjs';
import { API_CONFIG } from '../config/api.config';
import { 
  LoginRequest, 
  RegisterPatientRequest, 
  CreateMedecinRequest, 
  AuthResponse,
  User 
} from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = API_CONFIG.baseUrl;
  
  // Signaux privés
  private _currentUser = signal<User | null>(this.getUserFromStorage());
  private _userRole = signal<string | null>(localStorage.getItem('userRole'));
  private _isLoggedIn = signal<boolean>(!!localStorage.getItem('currentUser'));

  // Signaux publics en lecture seule (computed pour la réactivité)
  currentUser = computed(() => this._currentUser());
  userRole = computed(() => this._userRole());
  isLoggedIn = computed(() => this._isLoggedIn());

  constructor(private http: HttpClient) {}

  login(credentials: LoginRequest): Observable<{ 
    success: boolean; 
    message: string; 
    role: string | null;
    user: User | null 
  }> {
    return this.http.post<AuthResponse>(`${this.API_URL}${API_CONFIG.endpoints.auth.login}`, credentials)
      .pipe(
        map(response => {
          console.log('🔍 Réponse backend:', response);
          
          if (!response.success) {
            return {
              success: false,
              message: response.message,
              role: null,
              user: null
            };
          }

          const roles = response.roles || [];
          const role = roles.length > 0 ? roles[0] : null;
          
          if (!role) {
            return {
              success: false,
              message: 'Aucun rôle trouvé pour cet utilisateur',
              role: null,
              user: null
            };
          }

          const user: User = {
            email: response.email || credentials.email,
            userId: response.userId,
            nom: response.nomComplet?.split(' ').slice(1).join(' '),
            prenom: response.nomComplet?.split(' ')[0],
            roles: roles
          };

          this.saveUser(user, role);

          return {
            success: true,
            message: response.message,
            role: role,
            user: user
          };
        }),
        catchError(error => {
          console.error('❌ Erreur HTTP:', error);
          return of({
            success: false,
            message: 'Erreur de connexion au serveur',
            role: null,
            user: null
          });
        })
      );
  }

  registerPatient(data: RegisterPatientRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(
      `${this.API_URL}${API_CONFIG.endpoints.auth.registerPatient}`, 
      data
    );
  }

  createMedecin(data: CreateMedecinRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(
      `${this.API_URL}${API_CONFIG.endpoints.auth.createMedecin}`, 
      data
    );
  }

  logout(): void {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('userRole');
    this._currentUser.set(null);
    this._userRole.set(null);
    this._isLoggedIn.set(false);
  }

  private saveUser(user: User, role: string): void {
    localStorage.setItem('currentUser', JSON.stringify(user));
    localStorage.setItem('userRole', role);
    this._userRole.set(role);
    this._currentUser.set(user);
    this._isLoggedIn.set(true);
    console.log('💾 Sauvegardé:', { user, role });
  }

  private getUserFromStorage(): User | null {
    const stored = localStorage.getItem('currentUser');
    return stored ? JSON.parse(stored) : null;
  }

  getCurrentRole(): string | null {
    return this._userRole();
  }
}