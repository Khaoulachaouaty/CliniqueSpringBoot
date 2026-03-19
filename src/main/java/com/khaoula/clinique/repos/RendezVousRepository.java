package com.khaoula.clinique.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khaoula.clinique.entities.RendezVous;

public interface RendezVousRepository extends JpaRepository<RendezVous, Integer> {
	List<RendezVous> findByPatientId(int patientId);
	
	List<RendezVous> findByMedecinId(int medecinId);
	
	List<RendezVous> findByStatut(String statut);
	
	List<RendezVous> findByMedecinIdAndStatut(int medecinId, String statut);
	
	List<RendezVous> findByPatientIdAndStatut(int patientId, String statut);
}
