package com.khaoula.clinique.service;

import java.util.List;
import java.util.Optional;

import com.khaoula.clinique.entities.Notification;
import com.khaoula.clinique.entities.Patient;
import com.khaoula.clinique.entities.RendezVous;

public interface NotificationService {
    
    // CRUD de base
    Notification saveNotification(Notification notification);
    Notification updateNotification(Notification notification);
    void deleteNotification(Notification notification);
    void deleteNotificationById(Long id);
    Optional<Notification> getNotification(Long id);
    List<Notification> getAllNotifications();
    List<Notification> findByPatientId(Long patientId);
    
    /**
     * Envoie une notification (avec vérifications null)
     * @param message le message (ne doit pas être null)
     * @param type le type de notification
     * @param patient le patient destinataire (ne doit pas être null)
     * @param rendezVous le RDV associé (peut être null)
     */
    void envoyerNotification(String message, String type, Patient patient, RendezVous rendezVous);
    
    /**
     * Notifie d'un nouveau RDV (avec vérifications)
     * @param rdv le rendez-vous (doit avoir patient et medecin non null)
     */
    void notifierNouveauRDV(RendezVous rdv);
    
    /**
     * Notifie d'une confirmation de RDV (avec vérifications)
     * @param rdv le rendez-vous confirmé
     */
    void notifierRDVConfirme(RendezVous rdv);
}