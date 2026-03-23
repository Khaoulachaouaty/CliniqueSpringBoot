package com.khaoula.clinique.service;

import java.util.List;
import java.util.Optional;

import com.khaoula.clinique.entities.RendezVous;

public interface RendezVousService {
    RendezVous saveRendezVous(RendezVous rendezVous);
    RendezVous updateRendezVous(RendezVous rendezVous);
    void deleteRendezVous(RendezVous rendezVous);
    void deleteRendezVousById(Long id);
    Optional<RendezVous> getRendezVous(Long id);
    List<RendezVous> getAllRendezVous();
    List<RendezVous> findByPatientId(Long patientId);
    List<RendezVous> findByMedecinId(Long medecinId);
    List<RendezVous> findByStatut(String statut);
}