package com.khaoula.clinique.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khaoula.clinique.entities.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
	
	List<Notification> findByPatientId(int patientId);
	
	List<Notification> findByStatut(String statut);
	
	List<Notification> findByRendezVousId(int rendezVousId);
}
