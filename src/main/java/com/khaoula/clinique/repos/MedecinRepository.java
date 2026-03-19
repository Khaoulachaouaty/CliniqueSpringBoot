package com.khaoula.clinique.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khaoula.clinique.entities.Medecin;

public interface MedecinRepository extends JpaRepository<Medecin, Integer> {
	Optional<Medecin> findByEmail(String email);
	
	List<Medecin> findBySpecialite(String specialite);
	
	boolean existsByEmail(String email);
}
