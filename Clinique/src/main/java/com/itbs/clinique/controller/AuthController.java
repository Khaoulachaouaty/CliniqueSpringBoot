package com.itbs.clinique.controller;

import com.itbs.clinique.dto.*;
import com.itbs.clinique.services.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Authentification", description = "Inscription et connexion des utilisateurs")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
        summary = "Inscription d'un patient",
        description = "Crée un nouveau compte patient. Retourne les informations de l'utilisateur créé."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Patient créé avec succès",
            content = @Content(schema = @Schema(implementation = AuthResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "message": "Patient créé avec succès",
                      "success": true,
                      "userId": 5,
                      "email": "jean.dupont@email.com",
                      "nomComplet": "Jean Dupont",
                      "roles": ["PATIENT"],
                      "patientId": 3,
                      "medecinId": null
                    }"""))),
        @ApiResponse(responseCode = "400", description = "Données invalides ou email déjà utilisé",
            content = @Content)
    })
    @PostMapping("/register/patient")
    public ResponseEntity<AuthResponse> registerPatient(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Informations du patient à créer",
                required = true,
                content = @Content(examples = @ExampleObject(value = """
                    {
                      "email": "jean.dupont@email.com",
                      "password": "motdepasse123",
                      "nom": "Dupont",
                      "prenom": "Jean",
                      "tel": "0612345678",
                      "dateNaissance": "1990-05-15",
                      "dossierMedical": ""
                    }""")))
            @Valid @RequestBody RegisterPatientRequest request) {
        return ResponseEntity.ok(authService.registerPatient(request));
    }

    @Operation(
        summary = "Connexion",
        description = "Authentifie un utilisateur (patient, médecin ou admin) et retourne ses informations."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Connexion réussie",
            content = @Content(schema = @Schema(implementation = AuthResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "message": "Connexion réussie",
                      "success": true,
                      "userId": 1,
                      "email": "admin@clinique.com",
                      "nomComplet": "Administrateur",
                      "roles": ["ADMIN"],
                      "patientId": null,
                      "medecinId": null
                    }"""))),
        @ApiResponse(responseCode = "401", description = "Email ou mot de passe incorrect",
            content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Identifiants de connexion",
                required = true,
                content = @Content(examples = @ExampleObject(value = """
                    {
                      "email": "admin@clinique.com",
                      "password": "admin123"
                    }""")))
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
