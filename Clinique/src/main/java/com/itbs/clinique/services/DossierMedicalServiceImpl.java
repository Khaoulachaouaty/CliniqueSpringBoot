// DossierMedicalServiceImpl.java
package com.itbs.clinique.services;

import com.itbs.clinique.dto.*;
import com.itbs.clinique.entities.*;
import com.itbs.clinique.repositories.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DossierMedicalServiceImpl implements DossierMedicalService {
    
    private final PatientRepository patientRepository;
    private final ConsultationRepository consultationRepository;
    private final MedecinRepository medecinRepository;

    public DossierMedicalServiceImpl(PatientRepository patientRepository,
                                     ConsultationRepository consultationRepository,
                                     MedecinRepository medecinRepository) {
        this.patientRepository = patientRepository;
        this.consultationRepository = consultationRepository;
        this.medecinRepository = medecinRepository;
    }

    @Override
    public DossierMedicalResponse consulterDossier(Long patientId, Long medecinUserId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient non trouvé"));
        
        // Vérifier que le médecin a bien consulté ce patient (sécurité)
        Long vraiMedecinId = getMedecinIdFromUserId(medecinUserId);
        boolean aConsulte = consultationRepository
                .findByRendezVousPatientIdOrderByDateConsultationDesc(patientId)
                .stream()
                .anyMatch(c -> c.getRendezVous().getMedecin().getId().equals(vraiMedecinId));
        
        if (!aConsulte) {
            throw new RuntimeException("Vous n'avez pas consulté ce patient");
        }
        
        List<Consultation> consultations = consultationRepository
                .findByRendezVousPatientIdOrderByDateConsultationDesc(patientId);
        
        return DossierMedicalResponse.builder()
                .patientId(patient.getId())
                .patientNomComplet(patient.getNomComplet())
                .dateNaissance(patient.getDateNaissance())
                .dossierMedical(patient.getDossierMedical())
                .historiqueConsultations(consultations.stream()
                    .map(this::mapToResume)
                    .collect(Collectors.toList()))
                .build();
    }

    @Override
    public void mettreAJourDossierApresConsultation(Consultation consultation) {
        RendezVous rdv = consultation.getRendezVous();
        Patient patient = rdv.getPatient();
        Medecin medecin = rdv.getMedecin();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String entree = String.format(
            "Consultation du %s avec Dr. %s (%s)\nDiagnostic: %s\nTraitement: %s\nOrdonnance: %s",
            sdf.format(consultation.getDateConsultation()),
            medecin.getNomComplet(),
            medecin.getSpecialite(),
            consultation.getDiagnostic(),
            consultation.getTraitement(),
            consultation.getOrdonnance()
        );
        
        patient.ajouterAuDossierMedical(entree);
        patientRepository.save(patient);
    }
    
    private Long getMedecinIdFromUserId(Long userId) {
        return medecinRepository.findByUserUserId(userId)
                .map(Medecin::getId)
                .orElse(userId);
    }
    
    private ConsultationResumeResponse mapToResume(Consultation c) {
        return ConsultationResumeResponse.builder()
                .id(c.getId())
                .date(c.getDateConsultation())
                .medecinNom(c.getRendezVous().getMedecin().getNomComplet())
                .specialite(c.getRendezVous().getMedecin().getSpecialite())
                .diagnostic(c.getDiagnostic())
                .build();
    }
}