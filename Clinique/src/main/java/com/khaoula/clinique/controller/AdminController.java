package com.khaoula.clinique.controller;

import com.khaoula.clinique.dto.*;
import com.khaoula.clinique.services.AuthService;
import com.khaoula.clinique.services.MedecinService;
import com.khaoula.clinique.services.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    
 // NOUVEAU : Détails médecin avec stats
    @GetMapping("/medecins/{id}/details")
    public ResponseEntity<Map<String, Object>> getMedecinDetails(@PathVariable Long id) {
        return ResponseEntity.ok(medecinService.getMedecinDetails(id));
    }

    // NOUVEAU : Supprimer médecin
    @DeleteMapping("/medecins/{id}")
    public ResponseEntity<MessageResponse> deleteMedecin(@PathVariable Long id) {
        medecinService.deleteMedecin(id);
        return ResponseEntity.ok(new MessageResponse("Médecin supprimé avec succès", true));
    }

    // NOUVEAU : Get médecin by ID (pour modification)
    @GetMapping("/medecins/{id}")
    public ResponseEntity<MedecinResponse> getMedecinById(@PathVariable Long id) {
        return ResponseEntity.ok(medecinService.getMedecinById(id));
    }
}
