package com.khaoula.clinique.service;

import java.util.List;
import java.util.Optional;

import com.khaoula.clinique.entities.Disponibilite;

public interface DisponibiliteService {
	Disponibilite saveDisponibilite(Disponibilite d);
	Disponibilite updateDisponibilite(Disponibilite d);
	void deleteDisponibilite(Disponibilite d);
	void deleteDisponibiliteById(int id);
	Optional<Disponibilite> getDisponibilite(int id);
	List<Disponibilite> getAllDisponibilites();
	List<Disponibilite> findByMedecinId(int medecinId);
	List<Disponibilite> findByMedecinAndJour(int medecinId, String jour);
}