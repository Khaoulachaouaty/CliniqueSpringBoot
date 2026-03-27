package com.khaoula.clinique.repositories;

import com.khaoula.clinique.entities.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
    
    // Compter les rendez-vous d'un médecin
    int countByMedecinId(Long medecinId);
    
    // Compter les patients distincts d'un médecin
    @Query("SELECT COUNT(DISTINCT r.patient.id) FROM RendezVous r WHERE r.medecin.id = :medecinId")
    int countDistinctPatientsByMedecinId(@Param("medecinId") Long medecinId);
}