package com.khaoula.clinique.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khaoula.clinique.entities.Notification;
import com.khaoula.clinique.entities.Patient;
import com.khaoula.clinique.entities.RendezVous;
import com.khaoula.clinique.repos.NotificationRepository;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public Notification updateNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(Notification notification) {
        notificationRepository.delete(notification);
    }

    @Override
    public void deleteNotificationById(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public Optional<Notification> getNotification(Long id) {
        return notificationRepository.findById(id);
    }

    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public List<Notification> findByPatientId(Long patientId) {
        return notificationRepository.findByPatientId(patientId);
    }

    @Override
    public void envoyerNotification(String message, String type, Patient patient, RendezVous rendezVous) {
        // ✅ Vérification null
        if (patient == null) {
            System.err.println("❌ Erreur: Patient null, notification annulée");
            return;
        }
        
        if (message == null || message.isEmpty()) {
            System.err.println("❌ Erreur: Message vide, notification annulée");
            return;
        }
        
        Notification notif = new Notification();
        notif.setMessage(message);
        notif.setDateEnvoi(new Date());
        notif.setType(type != null ? type : "GENERIQUE");
        notif.setStatut("ENVOYE");
        
        // ✅ Vérification email patient
        String email = patient.getEmail();
        if (email == null || email.isEmpty()) {
            email = "inconnu@clinique.com";
            System.err.println("⚠️ Warning: Email patient inconnu, utilisant: " + email);
        }
        notif.setDestinataire(email);
        
        notif.setPatient(patient);
        notif.setRendezVous(rendezVous); // peut être null
        
        notificationRepository.save(notif);
        
        System.out.println("✅ NOTIFICATION [" + notif.getType() + "] envoyée à " + email);
    }

    @Override
    public void notifierNouveauRDV(RendezVous rdv) {
        // ✅ Vérification complète du RDV
        if (rdv == null) {
            System.err.println("❌ Erreur: RendezVous null");
            return;
        }
        
        if (rdv.getPatient() == null) {
            System.err.println("❌ Erreur: Patient null dans le RDV");
            return;
        }
        
        if (rdv.getMedecin() == null) {
            System.err.println("❌ Erreur: Medecin null dans le RDV");
            return;
        }
        
        // ✅ Construction sécurisée du message
        String nomMedecin = rdv.getMedecin().getNom() != null 
            ? rdv.getMedecin().getNom() 
            : "Dr Inconnu";
            
        String dateRDV = rdv.getDate() != null 
            ? rdv.getDate().toString() 
            : "date inconnue";
        
        String msg = "Votre RDV avec Dr " + nomMedecin 
                   + " du " + dateRDV + " est en attente de confirmation.";
        
        envoyerNotification(msg, "CONFIRMATION_RDV", rdv.getPatient(), rdv);
    }

    @Override
    public void notifierRDVConfirme(RendezVous rdv) {
        // ✅ Vérification complète
        if (rdv == null) {
            System.err.println("❌ Erreur: RendezVous null");
            return;
        }
        
        if (rdv.getPatient() == null) {
            System.err.println("❌ Erreur: Patient null dans le RDV");
            return;
        }
        
        // ✅ Construction sécurisée
        String dateRDV = rdv.getDate() != null 
            ? rdv.getDate().toString() 
            : "date inconnue";
            
        String heureRDV = rdv.getHeure() != null 
            ? rdv.getHeure() 
            : "heure inconnue";
        
        String msg = "Votre RDV est confirmé pour le " + dateRDV + " à " + heureRDV;
        
        envoyerNotification(msg, "RDV_CONFIRME", rdv.getPatient(), rdv);
    }
}