package com.khaoula.clinique.services;

import com.khaoula.clinique.dto.MedecinResponse;
import java.util.List;

public interface MedecinService {
    List<MedecinResponse> getAllMedecins();
    MedecinResponse getMedecinById(Long id);
    List<MedecinResponse> getMedecinsBySpecialite(String specialite);
}