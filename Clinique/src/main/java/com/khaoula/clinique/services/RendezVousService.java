package com.khaoula.clinique.services;

import com.khaoula.clinique.dto.*;
import java.util.Date;
import java.util.List;

public interface RendezVousService {
    
    // ========== PATIENT ==========
    RendezVousResponse createRendezVous(RendezVousRequest request);
    List<RendezVousResponse> getRendezVousByPatient(Long patientUserId);
    void cancelRendezVous(Long rendezVousId, Long patientUserId);
    
    // ========== MEDECIN ==========
    List<RendezVousResponse> getRendezVousByMedecin(Long medecinUserId);
    RendezVousResponse updateStatus(Long rendezVousId, String status);
    List<RendezVousResponse> getRendezVousDuJour(Long medecinUserId, Date date);
    List<CalendarEventResponse> getCalendarEvents(Long medecinUserId, Date start, Date end);
    
    // ========== CRÉNEAUX ==========
    List<String> getCreneauxDisponibles(Long medecinUserId, Date date);
    List<String> getCreneauxOccupes(Long medecinUserId, Date date);
    
    // ========== ADMIN ==========
    List<RendezVousResponse> getAllRendezVous();
    List<RendezVousResponse> filterByPatient(Long patientId);
    List<RendezVousResponse> filterBySpecialite(String specialite);
    List<RendezVousResponse> filterByMedecinAndDate(Long medecinId, Date date);
}