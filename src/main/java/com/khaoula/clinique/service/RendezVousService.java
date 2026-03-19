package com.khaoula.clinique.service;

import java.util.List;
import java.util.Optional;

import com.khaoula.clinique.entities.RendezVous;

public interface RendezVousService {
	RendezVous saveRendezVous(RendezVous r);
	RendezVous updateRendezVous(RendezVous r);
	void deleteRendezVous(RendezVous r);
	void deleteRendezVousById(int id);
	Optional<RendezVous> getRendezVous(int id);
	List<RendezVous> getAllRendezVous();
	List<RendezVous> findByPatientId(int patientId);
	List<RendezVous> findByMedecinId(int medecinId);
	List<RendezVous> findByStatut(String statut);
	List<RendezVous> findByMedecinAndStatut(int medecinId, String statut);
	List<RendezVous> findByPatientAndStatut(int patientId, String statut);
}