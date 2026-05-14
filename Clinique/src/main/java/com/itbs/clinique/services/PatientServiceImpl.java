package com.itbs.clinique.services;

import com.itbs.clinique.dto.PatientResponse;
import com.itbs.clinique.entities.Patient;
import com.itbs.clinique.entities.User;
import com.itbs.clinique.repositories.ConsultationRepository;
import com.itbs.clinique.repositories.NotificationRepository;
import com.itbs.clinique.repositories.PatientRepository;
import com.itbs.clinique.repositories.RendezVousRepository;
import com.itbs.clinique.repositories.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final RendezVousRepository rendezVousRepository;
    private final ConsultationRepository consultationRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public PatientServiceImpl(PatientRepository patientRepository,
                              RendezVousRepository rendezVousRepository,
                              ConsultationRepository consultationRepository,
                              NotificationRepository notificationRepository,
                              UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.rendezVousRepository = rendezVousRepository;
        this.consultationRepository = consultationRepository;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PatientResponse getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient non trouvé"));
        return mapToResponse(patient);
    }

    @Override
    @Transactional
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient non trouvé: " + id));

        Long userId = patient.getUser().getUserId();

        // 1. Supprimer les notifications du patient
        notificationRepository.findByPatientIdOrderByDateEnvoiDesc(id)
                .forEach(notificationRepository::delete);

        // 2. Supprimer les consultations liées aux rendez-vous du patient
        rendezVousRepository.findByPatientId(id).forEach(rdv -> {
            consultationRepository.findByRendezVousId(rdv.getId())
                    .ifPresent(consultationRepository::delete);
        });

        // 3. Supprimer les rendez-vous du patient
        rendezVousRepository.findByPatientId(id)
                .forEach(rendezVousRepository::delete);

        // 4. Supprimer le patient
        patientRepository.delete(patient);

        // 5. Supprimer le compte utilisateur
        userRepository.deleteById(userId);
    }

    private PatientResponse mapToResponse(Patient patient) {
        User user = patient.getUser();
        return PatientResponse.builder()
                .id(patient.getId())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .email(user.getUsername())
                .tel(user.getTel())
                .dateNaissance(patient.getDateNaissance())
                .dossierMedical(patient.getDossierMedical())
                .build();
    }
}
