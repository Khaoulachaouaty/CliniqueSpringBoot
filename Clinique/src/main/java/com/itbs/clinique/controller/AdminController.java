package com.itbs.clinique.controller;

import com.itbs.clinique.dto.*;
import com.itbs.clinique.services.AuthService;
import com.itbs.clinique.services.MedecinService;
import com.itbs.clinique.services.PatientService;
import com.itbs.clinique.services.RendezVousService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@Tag(name = "Administration", description = "Gestion des médecins et patients (accès admin)")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final AuthService authService;
    private final PatientService patientService;
    private final MedecinService medecinService;
    private final RendezVousService rendezVousService;

    public AdminController(AuthService authService,
                           PatientService patientService,
                           MedecinService medecinService,
                           RendezVousService rendezVousService) {
        this.authService = authService;
        this.patientService = patientService;
        this.medecinService = medecinService;
        this.rendezVousService = rendezVousService;
    }

    // ==================== MÉDECINS ====================

    @Operation(
        summary = "Créer un médecin",
        description = "Crée un nouveau compte médecin avec sa spécialité. Réservé à l'administrateur."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Médecin créé avec succès",
            content = @Content(schema = @Schema(implementation = AuthResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "message": "Médecin créé avec succès",
                      "success": true,
                      "userId": 10,
                      "email": "dr.martin@clinique.com",
                      "nomComplet": "Dr. Martin Sophie",
                      "roles": ["MEDECIN"],
                      "patientId": null,
                      "medecinId": 4
                    }"""))),
        @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content)
    })
    @PostMapping("/medecins")
    public ResponseEntity<AuthResponse> createMedecin(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Informations du médecin",
                required = true,
                content = @Content(examples = @ExampleObject(value = """
                    {
                      "email": "dr.martin@clinique.com",
                      "password": "motdepasse123",
                      "nom": "Martin",
                      "prenom": "Sophie",
                      "tel": "0698765432",
                      "specialite": "Cardiologie"
                    }""")))
            @Valid @RequestBody CreateMedecinRequest request) {
        logger.info("👨‍⚕️ Création médecin: {}", request.getEmail());
        return ResponseEntity.ok(authService.createMedecin(request));
    }

    @Operation(summary = "Lister tous les médecins", description = "Retourne la liste complète des médecins enregistrés.")
    @ApiResponse(responseCode = "200", description = "Liste des médecins",
        content = @Content(schema = @Schema(implementation = MedecinResponse.class)))
    @GetMapping("/medecins")
    public ResponseEntity<List<MedecinResponse>> getAllMedecins() {
        return ResponseEntity.ok(medecinService.getAllMedecins());
    }

    @Operation(summary = "Obtenir un médecin par ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Médecin trouvé",
            content = @Content(schema = @Schema(implementation = MedecinResponse.class))),
        @ApiResponse(responseCode = "404", description = "Médecin introuvable", content = @Content)
    })
    @GetMapping("/medecins/{id}")
    public ResponseEntity<MedecinResponse> getMedecinById(
            @Parameter(description = "ID du médecin", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(medecinService.getMedecinById(id));
    }

    @Operation(
        summary = "Détails complets d'un médecin",
        description = "Retourne les informations du médecin ainsi que ses statistiques (nombre de RDV, consultations, etc.)."
    )
    @ApiResponse(responseCode = "200", description = "Détails du médecin")
    @GetMapping("/medecins/{id}/details")
    public ResponseEntity<Map<String, Object>> getMedecinDetails(
            @Parameter(description = "ID du médecin", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(medecinService.getMedecinDetails(id));
    }

    @Operation(summary = "Supprimer un médecin", description = "Supprime définitivement un médecin et son compte utilisateur.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Médecin supprimé",
            content = @Content(schema = @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "404", description = "Médecin introuvable", content = @Content)
    })
    @DeleteMapping("/medecins/{id}")
    public ResponseEntity<MessageResponse> deleteMedecin(
            @Parameter(description = "ID du médecin à supprimer", required = true, example = "1")
            @PathVariable Long id) {
        medecinService.deleteMedecin(id);
        return ResponseEntity.ok(new MessageResponse("Médecin supprimé avec succès", true));
    }

    // ==================== PATIENTS ====================

    @Operation(summary = "Lister tous les patients", description = "Retourne la liste complète des patients enregistrés.")
    @ApiResponse(responseCode = "200", description = "Liste des patients",
        content = @Content(schema = @Schema(implementation = PatientResponse.class)))
    @GetMapping("/patients")
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }
}
