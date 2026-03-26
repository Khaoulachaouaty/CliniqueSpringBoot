package com.khaoula.clinique.repository;

import com.khaoula.clinique.entities.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
    List<RendezVous> findByPatientId(Long patientId);
    List<RendezVous> findByMedecinId(Long medecinId);
    List<RendezVous> findByMedecinIdAndDate(Long medecinId, Date date);
    List<RendezVous> findByDateAndStatut(Date date, String statut);
} 	