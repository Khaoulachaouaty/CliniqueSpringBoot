package com.khaoula.clinique.service;

import java.util.List;
import java.util.Optional;

import com.khaoula.clinique.entities.Notification;
import com.khaoula.clinique.entities.Patient;
import com.khaoula.clinique.entities.RendezVous;

public interface NotificationService {
	Notification saveNotification(Notification n);
	Notification updateNotification(Notification n);
	void deleteNotification(Notification n);
	void deleteNotificationById(int id);
	Optional<Notification> getNotification(int id);
	List<Notification> getAllNotifications();
	List<Notification> findByPatientId(int patientId);
	List<Notification> findByStatut(String statut);
	void envoyerNotification(String message, String type, Patient patient, RendezVous rendezVous);
	void notifierNouveauRDV(RendezVous rdv);
	void notifierRDVConfirme(RendezVous rdv);
}