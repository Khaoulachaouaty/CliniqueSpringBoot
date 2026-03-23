package com.khaoula.clinique.service;

import java.util.List;
import java.util.Optional;

import com.khaoula.clinique.entities.Disponibilite;

public interface DisponibiliteService {
    Disponibilite saveDisponibilite(Disponibilite disponibilite);
    Disponibilite updateDisponibilite(Disponibilite disponibilite);
    void deleteDisponibilite(Disponibilite disponibilite);
    void deleteDisponibiliteById(Long id);
    Optional<Disponibilite> getDisponibilite(Long id);
    List<Disponibilite> getAllDisponibilites();
    List<Disponibilite> findByMedecinId(Long medecinId);
}