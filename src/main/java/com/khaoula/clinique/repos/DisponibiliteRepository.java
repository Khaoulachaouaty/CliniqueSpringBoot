package com.khaoula.clinique.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.khaoula.clinique.entities.Disponibilite;

@Repository
public interface DisponibiliteRepository extends JpaRepository<Disponibilite, Integer> {
	
	List<Disponibilite> findByMedecinId(int medecinId);
	
	List<Disponibilite> findByMedecinIdAndJourSemaine(int medecinId, String jourSemaine);
}