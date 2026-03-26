package com.khaoula.clinique.service;

import com.khaoula.clinique.entities.Notification;
import java.util.List;

public interface NotificationService {
    Notification saveNotification(Notification notification);
    Notification envoyerNotificationPatient(Long patientId, String message);
    List<Notification> getNotificationsByPatient(Long patientId);
    List<Notification> getAllNotifications();
    void marquerCommeLue(Long id);
}