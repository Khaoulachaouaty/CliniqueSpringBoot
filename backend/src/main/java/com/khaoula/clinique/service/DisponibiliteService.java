package com.khaoula.clinique.service;

import com.khaoula.clinique.entities.Disponibilite;
import java.util.List;

public interface DisponibiliteService {
    Disponibilite saveDisponibilite(Disponibilite disponibilite);
    Disponibilite getDisponibiliteById(Long id);
    List<Disponibilite> getDisponibilitesByMedecin(Long medecinId);
    List<Disponibilite> getAllDisponibilites();
    void deleteDisponibilite(Long id);
}