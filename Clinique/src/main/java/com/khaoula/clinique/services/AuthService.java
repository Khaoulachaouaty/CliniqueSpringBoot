package com.khaoula.clinique.services;

import com.khaoula.clinique.dto.*;

public interface AuthService {
    AuthResponse registerPatient(RegisterPatientRequest request);
    AuthResponse createMedecin(CreateMedecinRequest request);
    AuthResponse login(LoginRequest request);
}