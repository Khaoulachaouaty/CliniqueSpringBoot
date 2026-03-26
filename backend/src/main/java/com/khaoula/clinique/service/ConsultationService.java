package com.khaoula.clinique.service;

import com.khaoula.clinique.entities.Consultation;
import java.util.List;

public interface ConsultationService {
    Consultation saveConsultation(Consultation consultation);
    Consultation getConsultationById(Long id);
    Consultation getConsultationByRendezVous(Long rendezVousId);
    List<Consultation> getAllConsultations();
    double getTotalFacture(Long consultationId);
}