package com.khaoula.clinique.service;

import java.util.List;
import java.util.Optional;

import com.khaoula.clinique.entities.Medecin;

public interface MedecinService {
	Medecin saveMedecin(Medecin m);
	Medecin updateMedecin(Medecin m);
	void deleteMedecin(Medecin m);
	void deleteMedecinById(int id);
	Optional<Medecin> getMedecin(int id);
	List<Medecin> getAllMedecins();
	List<Medecin> findBySpecialite(String specialite);
	Optional<Medecin> findByEmail(String email);
}