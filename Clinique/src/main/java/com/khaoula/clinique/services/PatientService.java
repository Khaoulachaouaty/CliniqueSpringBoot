package com.khaoula.clinique.services;

import com.khaoula.clinique.dto.PatientResponse;
import java.util.List;

public interface PatientService {
    List<PatientResponse> getAllPatients();
    PatientResponse getPatientById(Long id);
}