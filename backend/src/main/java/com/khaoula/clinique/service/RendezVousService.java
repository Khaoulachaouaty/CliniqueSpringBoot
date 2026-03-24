package com.khaoula.clinique.service;

import java.util.List;
import java.util.Optional;

import com.khaoula.clinique.entities.RendezVous;

public interface RendezVousService {
	
	RendezVous saveRendezVous(RendezVous r);
	RendezVous updateRendezVous(RendezVous r);
	void deleteRendezVous(RendezVous r);
	void deleteRendezVousById(Long id);
	Optional<RendezVous> getRendezVous(Long id);
	List<RendezVous> getAllRendezVous();
	List<RendezVous> findByPatientId(Long patientId);
	List<RendezVous> findByMedecinId(Long medecinId);
	List<RendezVous> findByStatut(String statut);
	List<RendezVous> findByMedecinAndStatut(Long medecinId, String statut);
	List<RendezVous> findByPatientAndStatut(Long patientId, String statut);
	
	// AJOUTER CETTE MÉTHODE :
	boolean verifierDisponibilite(RendezVous rdv);
}