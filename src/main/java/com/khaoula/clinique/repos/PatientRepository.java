package com.khaoula.clinique.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khaoula.clinique.entities.Patient;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
	Optional<Patient> findByEmail(String email);
	Optional<Patient> findByTel(String tel);
	boolean existsByEmail(String email);
}
