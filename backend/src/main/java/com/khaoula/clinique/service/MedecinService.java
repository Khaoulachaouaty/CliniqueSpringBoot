package com.khaoula.clinique.service;

import java.util.List;
import java.util.Optional;

import com.khaoula.clinique.entities.Medecin;

public interface MedecinService {
    Medecin saveMedecin(Medecin medecin);
    Medecin updateMedecin(Medecin medecin);
    void deleteMedecin(Medecin medecin);
    void deleteMedecinById(Long id);
    Optional<Medecin> getMedecin(Long id);
    List<Medecin> getAllMedecins();
    List<Medecin> findBySpecialite(String specialite);
}