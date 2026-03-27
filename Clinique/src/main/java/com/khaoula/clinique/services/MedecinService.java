package com.khaoula.clinique.services;

import com.khaoula.clinique.dto.MedecinResponse;
import java.util.List;
import java.util.Map;

public interface MedecinService {
    List<MedecinResponse> getAllMedecins();
    MedecinResponse getMedecinById(Long id);
    List<MedecinResponse> getMedecinsBySpecialite(String specialite);
    // NOUVEAUX
    Map<String, Object> getMedecinDetails(Long id);
    void deleteMedecin(Long id);
    // SUPPRIMÉ : findById (utilisez getMedecinById ou repository directement)
}