package com.khaoula.clinique.services;

import com.khaoula.clinique.dto.MedecinResponse;
import com.khaoula.clinique.entities.Medecin;
import com.khaoula.clinique.entities.User;
import com.khaoula.clinique.repositories.MedecinRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedecinServiceImpl implements MedecinService {
    
    private final MedecinRepository medecinRepository;

    public MedecinServiceImpl(MedecinRepository medecinRepository) {
        this.medecinRepository = medecinRepository;
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
}