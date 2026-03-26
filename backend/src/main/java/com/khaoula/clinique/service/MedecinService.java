package com.khaoula.clinique.service;

import com.khaoula.clinique.entities.Medecin;
import java.util.List;

public interface MedecinService {
    Medecin saveMedecin(Medecin medecin);
    Medecin getMedecinById(Long id);
    Medecin getMedecinByUsername(String username);
    List<Medecin> getAllMedecins();
    List<Medecin> getMedecinsBySpecialite(String specialite);
    void deleteMedecin(Long id);
}