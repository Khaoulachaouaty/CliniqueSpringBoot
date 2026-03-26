package com.khaoula.clinique.service;

import com.khaoula.clinique.entities.Patient;
import java.util.List;

public interface PatientService {
    Patient savePatient(Patient patient);
    Patient getPatientById(Long id);
    Patient getPatientByUsername(String username);
    List<Patient> getAllPatients();
    void deletePatient(Long id);
}