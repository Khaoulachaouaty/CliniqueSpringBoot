package com.itbs.clinique.services;

import java.util.List;
import java.util.Map;

import com.itbs.clinique.dto.MedecinResponse;
import com.itbs.clinique.dto.UpdateMedecinRequest;

public interface MedecinService {
    List<MedecinResponse> getAllMedecins();
    MedecinResponse getMedecinById(Long id);
    List<MedecinResponse> getMedecinsBySpecialite(String specialite);
    Map<String, Object> getMedecinDetails(Long id);
    void deleteMedecin(Long id);
    MedecinResponse updateMedecin(Long id, UpdateMedecinRequest request);
}