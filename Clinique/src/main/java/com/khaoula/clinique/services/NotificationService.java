package com.khaoula.clinique.services;

import com.khaoula.clinique.dto.*;
import com.khaoula.clinique.entities.*;
import java.util.List;

public interface NotificationService {
    
    // ========== NOTIFICATIONS AU MÉDECIN (actions du patient) ==========
    void notifierNouveauRendezVousMedecin(RendezVous rendezVous);
    void notifierAnnulationRdvParPatient(RendezVous rendezVous);
    
    // ========== NOTIFICATIONS AU PATIENT (actions du médecin) ==========
    void notifierConfirmationRdv(RendezVous rendezVous);
    void notifierAnnulationRdvParMedecin(RendezVous rendezVous, String raison);
    void notifierRappelRendezVous(RendezVous rendezVous);
    void notifierDemandeRendezVousRecue(RendezVous rendezVous);
    
    // ========== NOTIFICATIONS AU PATIENT (système/facturation) ==========
    void notifierNouvelleFacture(Consultation consultation);
    void notifierPaiementRecu(Consultation consultation);
    
    // ========== CRÉATION MANUELLE ==========
    NotificationResponse createNotification(NotificationRequest request);
    
    // ========== LECTURE ==========
    List<NotificationResponse> getNotificationsByPatient(Long patientId);
    List<NotificationResponse> getNotificationsByMedecin(Long medecinId);
    List<NotificationResponse> getNotificationsNonLues(Long userId, String userType);
    
    // ========== ACTIONS ==========
    NotificationResponse marquerCommeLue(Long notificationId);
    void marquerToutesCommeLues(Long userId, String userType);
    long getNombreNotificationsNonLues(Long userId, String userType);
    
    // ========== TÂCHE PLANIFIÉE ==========
    void envoyerRappelsAutomatiques();
}