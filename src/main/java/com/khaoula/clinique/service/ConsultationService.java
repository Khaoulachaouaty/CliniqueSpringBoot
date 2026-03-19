package com.khaoula.clinique.service;

import java.util.List;
import java.util.Optional;

import com.khaoula.clinique.entities.Consultation;

public interface ConsultationService {
	Consultation saveConsultation(Consultation c);
	Consultation updateConsultation(Consultation c);
	void deleteConsultation(Consultation c);
	void deleteConsultationById(int id);
	Optional<Consultation> getConsultation(int id);
	List<Consultation> getAllConsultations();
	Optional<Consultation> findByRendezVousId(int rendezVousId);
}