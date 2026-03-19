package com.khaoula.clinique.service;

import java.util.List;
import java.util.Optional;

import com.khaoula.clinique.entities.Patient;

public interface PatientService {
	Patient savePatient(Patient p);
	Patient updatePatient(Patient p);
	void deletePatient(Patient p);
	void deletePatientById(int id);
	Optional<Patient> getPatient(int id);
	List<Patient> getAllPatients();
	Optional<Patient> findByEmail(String email);
	boolean emailExists(String email);
}