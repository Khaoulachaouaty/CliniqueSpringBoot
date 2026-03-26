package com.khaoula.clinique.repository;

import com.khaoula.clinique.entities.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedecinRepository extends JpaRepository<Medecin, Long> {
    List<Medecin> findBySpecialite(String specialite);
    Medecin findByUserUsername(String username);
}