package com.itbs.clinique.controller;

import com.itbs.clinique.dto.MedecinResponse;
import com.itbs.clinique.services.MedecinService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "*")
@Tag(name = "Public", description = "Endpoints publics accessibles sans authentification")
public class PublicController {

    private final MedecinService medecinService;

    public PublicController(MedecinService medecinService) {
        this.medecinService = medecinService;
    }

    @Operation(
        summary = "Liste de tous les médecins",
        description = "Retourne la liste publique de tous les médecins de la clinique avec leurs spécialités."
    )
    @ApiResponse(responseCode = "200", description = "Liste des médecins",
        content = @Content(schema = @Schema(implementation = MedecinResponse.class)))
    @GetMapping("/medecins")
    public ResponseEntity<List<MedecinResponse>> getAllMedecins() {
        return ResponseEntity.ok(medecinService.getAllMedecins());
    }

    @Operation(
        summary = "Médecins par spécialité",
        description = "Filtre les médecins par spécialité médicale (ex: Cardiologie, Dermatologie, Pédiatrie)."
    )
    @ApiResponse(responseCode = "200", description = "Liste des médecins filtrés par spécialité",
        content = @Content(schema = @Schema(implementation = MedecinResponse.class)))
    @GetMapping("/medecins/specialite/{specialite}")
    public ResponseEntity<List<MedecinResponse>> getBySpecialite(
            @Parameter(description = "Spécialité médicale", required = true, example = "Cardiologie")
            @PathVariable String specialite) {
        return ResponseEntity.ok(medecinService.getMedecinsBySpecialite(specialite));
    }
}
