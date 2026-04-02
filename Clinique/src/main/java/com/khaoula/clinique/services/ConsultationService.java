package com.khaoula.clinique.services;

import com.khaoula.clinique.dto.*;
import java.util.List;

public interface ConsultationService {
    
    // CRUD Consultation
    ConsultationResponse createConsultation(ConsultationRequest request);
    ConsultationResponse getConsultationById(Long id);
    ConsultationResponse getConsultationByRendezVous(Long rendezVousId);
    List<ConsultationResponse> getConsultationsByMedecin(Long medecinId);
    List<ConsultationResponse> getConsultationsByPatient(Long patientId);
    
    // Facturation
    FactureResponse genererFacture(Long consultationId);
    List<FactureResponse> getFacturesByPatient(Long patientId);
    FactureResponse updateStatutPaiement(Long consultationId, String statut);
    
    // Statistiques
    double calculerRevenusMedecin(Long medecinId);
}