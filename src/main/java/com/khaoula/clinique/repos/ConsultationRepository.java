package com.khaoula.clinique.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khaoula.clinique.entities.Consultation;

public interface ConsultationRepository extends JpaRepository<Consultation, Integer> {
	Optional<Consultation> findByRendezVousId(int rendezVousId);
}
