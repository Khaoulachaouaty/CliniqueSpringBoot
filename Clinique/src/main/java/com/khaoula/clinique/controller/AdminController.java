package com.khaoula.clinique.controller;

import com.khaoula.clinique.dto.*;
import com.khaoula.clinique.services.AuthService;
import com.khaoula.clinique.services.MedecinService;
import com.khaoula.clinique.services.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    
    private final AuthService authService;
    private final PatientService patientService;
    private final MedecinService medecinService;

    public AdminController(AuthService authService, PatientService patientService,
                          MedecinService medecinService) {
        this.authService = authService;
        this.patientService = patientService;
        this.medecinService = medecinService;
    }

    @PostMapping("/medecins")
    public ResponseEntity<AuthResponse> createMedecin(
            @Valid @RequestBody CreateMedecinRequest request) {
        return ResponseEntity.ok(authService.createMedecin(request));
    }

    @GetMapping("/patients")
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/medecins")
    public ResponseEntity<List<MedecinResponse>> getAllMedecins() {
        return ResponseEntity.ok(medecinService.getAllMedecins());
    }
}