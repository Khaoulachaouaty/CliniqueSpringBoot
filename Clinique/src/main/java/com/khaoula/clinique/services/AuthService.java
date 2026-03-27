package com.khaoula.clinique.services;

import com.khaoula.clinique.dto.*;

public interface AuthService {
    MessageResponse registerPatient(RegisterPatientRequest request);
    MessageResponse createMedecin(CreateMedecinRequest request);
    MessageResponse login(LoginRequest request);
}