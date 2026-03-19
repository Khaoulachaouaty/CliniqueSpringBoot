package com.khaoula.clinique.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.khaoula.clinique.entities.Patient;
import com.khaoula.clinique.repos.PatientRepository;

@Service
public class PatientServiceImpl implements PatientService {
	
	private final PatientRepository patientRepository;
	
	public PatientServiceImpl(PatientRepository patientRepository) {
		super();
		this.patientRepository = patientRepository;
	}

	@Override
	public Patient savePatient(Patient p) {
		return patientRepository.save(p);
	}
	
	@Override
	public Patient updatePatient(Patient p) {
		return patientRepository.save(p);
	}
	
	@Override
	public void deletePatient(Patient p) {
		patientRepository.delete(p);
	}
	
	@Override
	public void deletePatientById(int id) {
		patientRepository.deleteById(id);
	}
	
	@Override
	public Optional<Patient> getPatient(int id) {
		return patientRepository.findById(id);
	}
	
	@Override
	public List<Patient> getAllPatients() {
		return patientRepository.findAll();
	}
	
	@Override
	public Optional<Patient> findByEmail(String email) {
		return patientRepository.findByEmail(email);
	}
	
	@Override
	public boolean emailExists(String email) {
		return patientRepository.existsByEmail(email);
	}
}