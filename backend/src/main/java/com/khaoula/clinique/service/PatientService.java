package com.khaoula.clinique.service;

import java.util.List;
import java.util.Optional;

import com.khaoula.clinique.entities.Patient;

public interface PatientService {
    Patient savePatient(Patient patient);
    Patient updatePatient(Patient patient);
    void deletePatient(Patient patient);
    void deletePatientById(Long id);
    Optional<Patient> getPatient(Long id);
    List<Patient> getAllPatients();
    Optional<Patient> findByUserId(Long userId);
}