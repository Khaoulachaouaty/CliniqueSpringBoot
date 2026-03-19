package com.khaoula.clinique.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.khaoula.clinique.entities.Notification;
import com.khaoula.clinique.entities.Patient;
import com.khaoula.clinique.entities.RendezVous;
import com.khaoula.clinique.repos.NotificationRepository;

@Service
public class NotificationServiceImpl implements NotificationService {
	
	private final NotificationRepository notificationRepository;
	
	public NotificationServiceImpl(NotificationRepository notificationRepository) {
		super();
		this.notificationRepository = notificationRepository;
	}

	
	@Override
	public Notification saveNotification(Notification n) {
		return notificationRepository.save(n);
	}
	
	@Override
	public Notification updateNotification(Notification n) {
		return notificationRepository.save(n);
	}
	
	@Override
	public void deleteNotification(Notification n) {
		notificationRepository.delete(n);
	}
	
	@Override
	public void deleteNotificationById(int id) {
		notificationRepository.deleteById(id);
	}
	
	@Override
	public Optional<Notification> getNotification(int id) {
		return notificationRepository.findById(id);
	}
	
	@Override
	public List<Notification> getAllNotifications() {
		return notificationRepository.findAll();
	}
	
	@Override
	public List<Notification> findByPatientId(int patientId) {
		return notificationRepository.findByPatientId(patientId);
	}
	
	@Override
	public List<Notification> findByStatut(String statut) {
		return notificationRepository.findByStatut(statut);
	}
	
	@Override
	public void envoyerNotification(String message, String type, Patient patient, RendezVous rendezVous) {
		Notification notif = new Notification();
		notif.setMessage(message);
		notif.setDateEnvoi(new Date());
		notif.setType(type);
		notif.setStatut("ENVOYE");
		notif.setDestinataire(patient.getEmail());
		notif.setPatient(patient);
		notif.setRendezVous(rendezVous);
		
		notificationRepository.save(notif);
		
		System.out.println("📧 NOTIFICATION [" + type + "] à " + patient.getEmail());
		System.out.println("   Message: " + message);
	}
	
	@Override
	public void notifierNouveauRDV(RendezVous rdv) {
		String msg = "Votre RDV avec Dr " + rdv.getMedecin().getNom() 
				   + " du " + rdv.getDate() + " est en attente de confirmation.";
		envoyerNotification(msg, "CONFIRMATION_RDV", rdv.getPatient(), rdv);
	}
	
	@Override
	public void notifierRDVConfirme(RendezVous rdv) {
		String msg = "Votre RDV est confirmé pour le " + rdv.getDate() + " à " + rdv.getHeure();
		envoyerNotification(msg, "RDV_CONFIRME", rdv.getPatient(), rdv);
	}
}