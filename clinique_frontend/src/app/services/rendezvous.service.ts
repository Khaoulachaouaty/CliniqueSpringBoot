// services/rendezvous.service.ts - VERSION NETTOYÉE
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../config/api.config';
import { 
  RendezVous, 
  RendezVousRequest,
  CalendarEvent,
  Consultation
} from '../models/rendezvous.model';

@Injectable({
  providedIn: 'root'
})
export class RendezVousService {
  private readonly API_URL = API_CONFIG.baseUrl;

  constructor(private http: HttpClient) {}

  // ==================== PATIENT ====================
  
  createRendezVous(request: RendezVousRequest): Observable<RendezVous> {
    return this.http.post<RendezVous>(`${this.API_URL}/rendezvous`, request);
  }

  getRendezVousByPatient(patientId: number): Observable<RendezVous[]> {
    return this.http.get<RendezVous[]>(`${this.API_URL}/rendezvous/patient/${patientId}`);
  }

  cancelRendezVous(rendezVousId: number, patientId: number): Observable<any> {
    return this.http.put(`${this.API_URL}/rendezvous/${rendezVousId}/cancel`, null, {
      params: { patientId: patientId.toString() }
    });
  }

  // ==================== MEDECIN ====================
  
  getRendezVousByMedecin(medecinId: number): Observable<RendezVous[]> {
    return this.http.get<RendezVous[]>(`${this.API_URL}/rendezvous/medecin/${medecinId}`);
  }

  getRendezVousDuJour(medecinId: number, date: string): Observable<RendezVous[]> {
    return this.http.get<RendezVous[]>(`${this.API_URL}/rendezvous/medecin/${medecinId}/today`, {
      params: { date }
    });
  }

  updateStatus(rendezVousId: number, statut: string): Observable<RendezVous> {
    return this.http.put<RendezVous>(`${this.API_URL}/rendezvous/${rendezVousId}/status`, { statut });
  }

  getCalendarEvents(medecinId: number, start: string, end: string): Observable<CalendarEvent[]> {
    return this.http.get<CalendarEvent[]>(`${this.API_URL}/rendezvous/medecin/${medecinId}/calendar`, {
      params: { start, end }
    });
  }

  // ==================== CRÉNEAUX ====================
  
  getCreneauxDisponibles(medecinId: number, date: string): Observable<string[]> {
    return this.http.get<string[]>(`${this.API_URL}/rendezvous/creneaux/${medecinId}`, {
      params: { date }
    });
  }

  getCreneauxOccupes(medecinId: number, date: string): Observable<string[]> {
    return this.http.get<string[]>(`${this.API_URL}/rendezvous/creneaux/${medecinId}/occupes`, {
      params: { date }
    });
  }

  // ==================== ADMIN ====================
  
  getAllRendezVous(): Observable<RendezVous[]> {
    return this.http.get<RendezVous[]>(`${this.API_URL}/rendezvous`);
  }

  filterByPatient(patientId: number): Observable<RendezVous[]> {
    return this.http.get<RendezVous[]>(`${this.API_URL}/rendezvous/filter/patient/${patientId}`);
  }

  filterBySpecialite(specialite: string): Observable<RendezVous[]> {
    return this.http.get<RendezVous[]>(`${this.API_URL}/rendezvous/filter/specialite/${specialite}`);
  }

  filterByMedecinAndDate(medecinId: number, date: string): Observable<RendezVous[]> {
    return this.http.get<RendezVous[]>(`${this.API_URL}/rendezvous/filter/medecin/${medecinId}/date`, {
      params: { date }
    });
  }

  // ==================== CONSULTATIONS ====================
  
  createConsultation(rendezVousId: number, consultation: Partial<Consultation>): Observable<Consultation> {
    return this.http.post<Consultation>(`${this.API_URL}/consultations`, {
      rendezVousId,
      ...consultation
    });
  }

  getConsultationByRendezVous(rendezVousId: number): Observable<Consultation> {
    return this.http.get<Consultation>(`${this.API_URL}/consultations/rendezvous/${rendezVousId}`);
  }
}