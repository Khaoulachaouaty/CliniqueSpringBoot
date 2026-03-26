package com.khaoula.clinique.repository;

import com.khaoula.clinique.entities.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    Consultation findByRendezVousId(Long rendezVousId);
}

