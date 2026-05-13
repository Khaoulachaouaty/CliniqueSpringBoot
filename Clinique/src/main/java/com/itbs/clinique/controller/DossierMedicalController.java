package com.itbs.clinique.controller;

import com.itbs.clinique.dto.DossierMedicalResponse;
import com.itbs.clinique.services.DossierMedicalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dossiers-medicaux")
@CrossOrigin(origins = "*")
@Tag(name = "Dossiers Médicaux", description = "Consultation et mise à jour des dossiers patients")
public class DossierMedicalController {

    private final DossierMedicalService dossierMedicalService;

    public DossierMedicalController(DossierMedicalService dossierMedicalService) {
        this.dossierMedicalService = dossierMedicalService;
    }

    @Operation(
        summary = "Consulter le dossier d'un patient (médecin)",
        description = "Permet à un médecin de consulter le dossier médical complet d'un patient, incluant l'historique des consultations."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dossier médical retourné",
            content = @Content(schema = @Schema(implementation = DossierMedicalResponse.class))),
        @ApiResponse(responseCode = "404", description = "Patient ou médecin introuvable", content = @Content)
    })
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<DossierMedicalResponse> consulterDossier(
            @Parameter(description = "ID du patient", required = true, example = "3")
            @PathVariable Long patientId,
            @Parameter(description = "ID du médecin consultant", required = true, example = "1")
            @RequestParam Long medecinId) {
        return ResponseEntity.ok(dossierMedicalService.consulterDossier(patientId, medecinId));
    }

    @Operation(
        summary = "Consulter son propre dossier (patient)",
        description = "Permet à un patient de consulter son propre dossier médical sans avoir besoin de l'ID d'un médecin."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dossier médical retourné",
            content = @Content(schema = @Schema(implementation = DossierMedicalResponse.class))),
        @ApiResponse(responseCode = "404", description = "Patient introuvable", content = @Content)
    })
    @GetMapping("/patient/mon-dossier/{patientId}")
    public ResponseEntity<DossierMedicalResponse> consulterMonDossier(
            @Parameter(description = "ID du patient", required = true, example = "3")
            @PathVariable Long patientId) {
        return ResponseEntity.ok(dossierMedicalService.consulterMonDossier(patientId));
    }

    @Operation(
        summary = "Mettre à jour le dossier médical",
        description = "Permet à un médecin de mettre à jour les informations du dossier médical d'un patient (antécédents, allergies, notes, etc.)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dossier mis à jour",
            content = @Content(schema = @Schema(implementation = DossierMedicalResponse.class))),
        @ApiResponse(responseCode = "403", description = "Non autorisé", content = @Content),
        @ApiResponse(responseCode = "404", description = "Patient introuvable", content = @Content)
    })
    @PutMapping("/patient/{patientId}")
    public ResponseEntity<DossierMedicalResponse> updateDossierMedical(
            @Parameter(description = "ID du patient", required = true, example = "3")
            @PathVariable Long patientId,
            @Parameter(description = "ID du médecin", required = true, example = "1")
            @RequestParam Long medecinId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Contenu du dossier médical (texte libre ou JSON)",
                required = true,
                content = @Content(examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "\"Antécédents: HTA. Allergies: Pénicilline. Groupe sanguin: A+\"")))
            @RequestBody String dossierMedical) {
        return ResponseEntity.ok(dossierMedicalService.updateDossierMedical(patientId, medecinId, dossierMedical));
    }
}
