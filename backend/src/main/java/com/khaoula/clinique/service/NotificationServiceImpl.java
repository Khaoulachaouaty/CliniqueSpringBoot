package com.khaoula.clinique.service;

import com.khaoula.clinique.entities.Notification;
import com.khaoula.clinique.entities.Patient;
import com.khaoula.clinique.repository.NotificationRepository;
import com.khaoula.clinique.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Override
    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }
    
    @Override
    public Notification envoyerNotificationPatient(Long patientId, String message) {
        Patient patient = patientRepository.findById(patientId).orElse(null);
        if (patient != null) {
            Notification notif = new Notification();
            notif.setPatient(patient);
            notif.setMessage(message);
            notif.setDateEnvoi(new Date());
            notif.setType("RAPPEL");
            notif.setStatut("ENVOYE");
            notif.setDestinataire(patient.getEmail());
            return notificationRepository.save(notif);
        }
        return null;
    }
    
    @Override
    public List<Notification> getNotificationsByPatient(Long patientId) {
        return notificationRepository.findByPatientId(patientId);
    }
    
    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
    
    @Override
    public void marquerCommeLue(Long id) {
        Notification notif = notificationRepository.findById(id).orElse(null);
        if (notif != null) {
            notif.setStatut("LU");
            notificationRepository.save(notif);
        }
    }
}