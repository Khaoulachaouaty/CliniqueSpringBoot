// models/rendezvous.model.ts
export interface RendezVous {
  id: number;
  date: string;
  heure: string;
  motif: string;
  statut: 'EN_ATTENTE' | 'CONFIRME' | 'ANNULE' | 'TERMINE';
  
  // Patient info
  patientId: number;
  patientNom: string;
  patientPrenom: string;
  patientEmail: string;
  patientTel: string;
  
  // Médecin info
  medecinId: number;
  medecinNom: string;
  medecinPrenom: string;
  medecinSpecialite: string;
}

export interface RendezVousRequest {
  patientId: number;
  medecinId: number;
  date: string;
  heure: string;
  motif: string;
}

// SUPPRIMÉ : Disponibilite et DisponibiliteRequest (plus d'entité Disponibilite)

export interface CalendarEvent {
  id: number;
  title: string;
  start: string;
  end: string;
  status: string;
  color: string;
  patientId: number;
  patientNom: string;
  motif: string;
}

export interface Consultation {
  id: number;
  diagnostic: string;
  ordonnance: string;
  prix: number;
  rendezVousId: number;
  date: string;
  patientNom: string;
  medecinNom: string;
}
