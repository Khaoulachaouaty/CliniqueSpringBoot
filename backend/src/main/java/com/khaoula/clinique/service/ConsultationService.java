package com.khaoula.clinique.service;

import java.util.List;
import java.util.Optional;

import com.khaoula.clinique.entities.Consultation;

public interface ConsultationService {
    Consultation saveConsultation(Consultation consultation);
    Consultation updateConsultation(Consultation consultation);
    void deleteConsultation(Consultation consultation);
    void deleteConsultationById(Long id);
    Optional<Consultation> getConsultation(Long id);
    List<Consultation> getAllConsultations();
    Optional<Consultation> findByRendezVousId(Long rendezVousId);
}