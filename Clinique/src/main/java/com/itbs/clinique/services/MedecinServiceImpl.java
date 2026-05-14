package com.itbs.clinique.services;

import com.itbs.clinique.dto.MedecinResponse;
import com.itbs.clinique.dto.UpdateMedecinRequest;
import com.itbs.clinique.entities.Medecin;
import com.itbs.clinique.entities.User;
import com.itbs.clinique.repositories.ConsultationRepository;
import com.itbs.clinique.repositories.MedecinRepository;
import com.itbs.clinique.repositories.NotificationRepository;
import com.itbs.clinique.repositories.RendezVousRepository;
import com.itbs.clinique.repositories.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MedecinServiceImpl implements MedecinService {

    private final MedecinRepository medecinRepository;
    private final RendezVousRepository rendezVousRepository;
    private final ConsultationRepository consultationRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public MedecinServiceImpl(MedecinRepository medecinRepository,
                              RendezVousRepository rendezVousRepository,
                              ConsultationRepository consultationRepository,
                              NotificationRepository notificationRepository,
                              UserRepository userRepository) {
        this.medecinRepository = medecinRepository;
        this.rendezVousRepository = rendezVousRepository;
        this.consultationRepository = consultationRepository;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<MedecinResponse> getAllMedecins() {
        return medecinRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MedecinResponse getMedecinById(Long id) {
        Medecin medecin = medecinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé"));
        return mapToResponse(medecin);
    }

    @Override
    public List<MedecinResponse> getMedecinsBySpecialite(String specialite) {
        return medecinRepository.findBySpecialite(specialite).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getMedecinDetails(Long id) {
        Medecin medecin = medecinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé"));

        int nombrePatients = rendezVousRepository.countDistinctPatientsByMedecinId(id);
        int rendezVousTotal = rendezVousRepository.countByMedecinId(id);

        Map<String, Object> details = new HashMap<>();
        details.put("medecin", mapToResponse(medecin));
        details.put("nombrePatients", nombrePatients);
        details.put("rendezVousTotal", rendezVousTotal);

        return details;
    }

    @Override
    @Transactional
    public void deleteMedecin(Long id) {
        Medecin medecin = medecinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé: " + id));

        Long userId = medecin.getUser().getUserId();

        // 1. Supprimer les notifications liées au médecin
        notificationRepository.findByMedecinIdOrderByDateEnvoiDesc(id)
                .forEach(notificationRepository::delete);

        // 2. Supprimer les consultations liées aux rendez-vous du médecin
        rendezVousRepository.findByMedecinId(id).forEach(rdv -> {
            consultationRepository.findByRendezVousId(rdv.getId())
                    .ifPresent(consultationRepository::delete);
        });

        // 3. Supprimer les rendez-vous du médecin
        rendezVousRepository.findByMedecinId(id)
                .forEach(rendezVousRepository::delete);

        // 4. Supprimer le médecin
        medecinRepository.delete(medecin);

        // 5. Supprimer le compte utilisateur
        userRepository.deleteById(userId);
    }

    private MedecinResponse mapToResponse(Medecin medecin) {
        User user = medecin.getUser();
        return MedecinResponse.builder()
                .id(medecin.getId())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .email(user.getUsername())
                .tel(user.getTel())
                .specialite(medecin.getSpecialite())
                .build();
    }

    @Override
    @Transactional
    public MedecinResponse updateMedecin(Long id, UpdateMedecinRequest request) {
        Medecin medecin = medecinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé: " + id));

        User user = medecin.getUser();
        if (request.getNom() != null)       user.setNom(request.getNom());
        if (request.getPrenom() != null)    user.setPrenom(request.getPrenom());
        if (request.getTel() != null)       user.setTel(request.getTel());
        if (request.getSpecialite() != null) medecin.setSpecialite(request.getSpecialite());

        medecinRepository.save(medecin);
        return mapToResponse(medecin);
    }
}
