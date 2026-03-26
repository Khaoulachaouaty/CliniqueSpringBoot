package com.khaoula.clinique.service;

import com.khaoula.clinique.entities.RendezVous;
import java.util.Date;
import java.util.List;

public interface RendezVousService {
    RendezVous saveRendezVous(RendezVous rendezVous);
    RendezVous getRendezVousById(Long id);
    List<RendezVous> getRendezVousByPatient(Long patientId);
    List<RendezVous> getRendezVousByMedecin(Long medecinId);
    List<RendezVous> getRendezVousByMedecinAndDate(Long medecinId, Date date);
    List<RendezVous> getAllRendezVous();
    RendezVous accepterRendezVous(Long id);
    RendezVous refuserRendezVous(Long id);
    void deleteRendezVous(Long id);
    boolean verifierDisponibilite(Long medecinId, Date date, String heure);
}