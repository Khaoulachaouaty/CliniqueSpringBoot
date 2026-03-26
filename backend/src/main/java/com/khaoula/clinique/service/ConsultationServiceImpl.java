package com.khaoula.clinique.service;

import com.khaoula.clinique.entities.Consultation;
import com.khaoula.clinique.repository.ConsultationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultationServiceImpl implements ConsultationService {
    
    @Autowired
    private ConsultationRepository consultationRepository;
    
    @Override
    public Consultation saveConsultation(Consultation consultation) {
        return consultationRepository.save(consultation);
    }
    
    @Override
    public Consultation getConsultationById(Long id) {
        return consultationRepository.findById(id).orElse(null);
    }
    
    @Override
    public Consultation getConsultationByRendezVous(Long rendezVousId) {
        return consultationRepository.findByRendezVousId(rendezVousId);
    }
    
    @Override
    public List<Consultation> getAllConsultations() {
        return consultationRepository.findAll();
    }
    
    @Override
    public double getTotalFacture(Long consultationId) {
        Consultation consultation = getConsultationById(consultationId);
        return consultation != null ? consultation.getPrix() : 0.0;
    }
} 	