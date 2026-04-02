// services/notification.service.ts

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, interval } from 'rxjs';
import { switchMap, catchError, tap } from 'rxjs/operators';
import { API_CONFIG } from '../config/api.config';
import { 
  NotificationResponse, 
  NotificationRequest, 
  MessageResponse 
} from '../models/notification.model';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  
  private readonly baseUrl = `${API_CONFIG.baseUrl}/notifications`;
  
  // BehaviorSubjects pour state management réactif
  private unreadCountPatient$ = new BehaviorSubject<number>(0);
  private unreadCountMedecin$ = new BehaviorSubject<number>(0);
  private notificationsPatient$ = new BehaviorSubject<NotificationResponse[]>([]);
  private notificationsMedecin$ = new BehaviorSubject<NotificationResponse[]>([]);

  // Observables publics
  public unreadCountPatient = this.unreadCountPatient$.asObservable();
  public unreadCountMedecin = this.unreadCountMedecin$.asObservable();
  public notificationsPatient = this.notificationsPatient$.asObservable();
  public notificationsMedecin = this.notificationsMedecin$.asObservable();

  constructor(private http: HttpClient) {}

  // ==================== CRÉATION ====================
  
  createNotification(request: NotificationRequest): Observable<NotificationResponse> {
    return this.http.post<NotificationResponse>(this.baseUrl, request);
  }

  // ==================== PATIENT ====================

getNotificationsByPatient(patientId: number): Observable<NotificationResponse[]> {
  
  // 🔥 PROTECTION CONTRE userId
  const userId = JSON.parse(localStorage.getItem('currentUser') || '{}')?.userId;

  if (patientId === userId) {
    console.error('❌ ERREUR: userId utilisé au lieu de patientId !', patientId);
  }

  const url = `${this.baseUrl}/patient/${patientId}`;
  console.log('🚀 URL FINALE:', url);

  return this.http.get<NotificationResponse[]>(url);
}

  getUnreadNotificationsPatient(patientId: number): Observable<NotificationResponse[]> {
    return this.http.get<NotificationResponse[]>(`${this.baseUrl}/patient/${patientId}/non-lues`);
  }

  getUnreadCountPatient(patientId: number): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/patient/${patientId}/count-non-lues`)
      .pipe(
        tap(count => this.unreadCountPatient$.next(count))
      );
  }

  markAsReadPatient(notificationId: number): Observable<NotificationResponse> {
    return this.http.put<NotificationResponse>(`${this.baseUrl}/${notificationId}/lue`, {});
  }

  markAllAsReadPatient(patientId: number): Observable<MessageResponse> {
    return this.http.put<MessageResponse>(`${this.baseUrl}/patient/${patientId}/tout-lire`, {})
      .pipe(
        tap(() => {
          this.unreadCountPatient$.next(0);
          // Rafraîchir la liste
          this.getNotificationsByPatient(patientId).subscribe();
        })
      );
  }

  // ==================== MÉDECIN ====================

  getNotificationsByMedecin(medecinId: number): Observable<NotificationResponse[]> {
    return this.http.get<NotificationResponse[]>(`${this.baseUrl}/medecin/${medecinId}`)
      .pipe(
        tap(notifs => this.notificationsMedecin$.next(notifs))
      );
  }

  getUnreadNotificationsMedecin(medecinId: number): Observable<NotificationResponse[]> {
    return this.http.get<NotificationResponse[]>(`${this.baseUrl}/medecin/${medecinId}/non-lues`);
  }

  getUnreadCountMedecin(medecinId: number): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/medecin/${medecinId}/count-non-lues`)
      .pipe(
        tap(count => this.unreadCountMedecin$.next(count))
      );
  }

  markAsReadMedecin(notificationId: number): Observable<NotificationResponse> {
    return this.http.put<NotificationResponse>(`${this.baseUrl}/${notificationId}/lue`, {});
  }

  markAllAsReadMedecin(medecinId: number): Observable<MessageResponse> {
    return this.http.put<MessageResponse>(`${this.baseUrl}/medecin/${medecinId}/tout-lire`, {})
      .pipe(
        tap(() => {
          this.unreadCountMedecin$.next(0);
          this.getNotificationsByMedecin(medecinId).subscribe();
        })
      );
  }

  // ==================== POLLING AUTO ====================

  startPollingPatient(patientId: number, intervalMs: number = 30000): Observable<number> {
    return interval(intervalMs).pipe(
      switchMap(() => this.getUnreadCountPatient(patientId)),
      catchError(err => {
        console.error('Erreur polling notifications patient:', err);
        return this.unreadCountPatient$;
      })
    );
  }

  startPollingMedecin(medecinId: number, intervalMs: number = 30000): Observable<number> {
    return interval(intervalMs).pipe(
      switchMap(() => this.getUnreadCountMedecin(medecinId)),
      catchError(err => {
        console.error('Erreur polling notifications médecin:', err);
        return this.unreadCountMedecin$;
      })
    );
  }

  // ==================== SIMULATION (DEV) ====================

  simulerRappels(): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(`${this.baseUrl}/simuler/rappels`, {});
  }

  // ==================== HELPERS ====================

  getIconForType(type: string): string {
    const icons: { [key: string]: string } = {
      'NOUVEAU_RDV': 'event_available',
      'CONFIRMATION_RDV': 'check_circle',
      'ANNULATION_PAR_PATIENT': 'cancel',
      'ANNULATION_PAR_MEDECIN': 'cancel',
      'RAPPEL_RDV': 'alarm',
      'DEMANDE_EN_ATTENTE': 'hourglass_empty',
      'FACTURE': 'receipt',
      'PAIEMENT_RECU': 'payments'
    };
    return icons[type] || 'notifications';
  }

  getColorForType(type: string): string {
    const colors: { [key: string]: string } = {
      'NOUVEAU_RDV': 'primary',
      'CONFIRMATION_RDV': 'success',
      'ANNULATION_PAR_PATIENT': 'warn',
      'ANNULATION_PAR_MEDECIN': 'warn',
      'RAPPEL_RDV': 'accent',
      'DEMANDE_EN_ATTENTE': 'warning',
      'FACTURE': 'primary',
      'PAIEMENT_RECU': 'success'
    };
    return colors[type] || 'default';
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    const now = new Date();
    const diff = now.getTime() - date.getTime();
    
    // Moins d'une minute
    if (diff < 60000) return 'À l\'instant';
    // Moins d'une heure
    if (diff < 3600000) return `Il y a ${Math.floor(diff / 60000)} min`;
    // Moins de 24h
    if (diff < 86400000) return `Il y a ${Math.floor(diff / 3600000)} h`;
    // Moins d'une semaine
    if (diff < 604800000) return `Il y a ${Math.floor(diff / 86400000)} j`;
    
    return date.toLocaleDateString('fr-FR', {
      day: 'numeric',
      month: 'short',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}