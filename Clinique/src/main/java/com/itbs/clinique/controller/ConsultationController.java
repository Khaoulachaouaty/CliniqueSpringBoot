package com.itbs.clinique.controller;

import com.itbs.clinique.dto.*;
import com.itbs.clinique.services.ConsultationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/consultations")
@CrossOrigin(origins = "*")
@Tag(name = "Consultations", description = "Gestion des consultations médicales et facturation")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('MEDECIN', 'ADMIN')")
public class ConsultationController {

    private static final Logger logger = LoggerFactory.getLogger(ConsultationController.class);

    private final ConsultationService consultationService;

    public ConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    // ==================== CONSULTATIONS ====================

    @Operation(
        summary = "Créer une consultation",
        description = "Enregistre une consultation médicale suite à un rendez-vous confirmé."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Consultation créée",
            content = @Content(schema = @Schema(implementation = ConsultationResponse.class))),
        @ApiResponse(responseCode = "400", description = "Rendez-vous introuvable ou déjà consulté", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ConsultationResponse> createConsultation(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Données de la consultation",
                required = true,
                content = @Content(examples = @ExampleObject(value = """
                    {
                      "rendezVousId": 12,
                      "diagnostic": "Hypertension artérielle légère",
                      "ordonnance": "Amlodipine 5mg - 1 comprimé/jour",
                      "traitement": "Traitement antihypertenseur",
                      "notes": "Contrôle dans 1 mois",
                      "prixConsultation": 60.0,
                      "montantMedicaments": 25.0
                    }""")))
            @Valid @RequestBody ConsultationRequest request) {
        logger.info("📝 Nouvelle consultation pour RDV: {}", request.getRendezVousId());
        return ResponseEntity.ok(consultationService.createConsultation(request));
    }

    @Operation(summary = "Obtenir une consultation par ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Consultation trouvée",
            content = @Content(schema = @Schema(implementation = ConsultationResponse.class))),
        @ApiResponse(responseCode = "404", description = "Consultation introuvable", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ConsultationResponse> getConsultation(
            @Parameter(description = "ID de la consultation", required = true, example = "5")
            @PathVariable Long id) {
        return ResponseEntity.ok(consultationService.getConsultationById(id));
    }

    @Operation(summary = "Consultation par rendez-vous", description = "Retourne la consultation associée à un rendez-vous.")
    @GetMapping("/rendezvous/{rendezVousId}")
    public ResponseEntity<ConsultationResponse> getByRendezVous(
            @Parameter(description = "ID du rendez-vous", required = true, example = "12")
            @PathVariable Long rendezVousId) {
        return ResponseEntity.ok(consultationService.getConsultationByRendezVous(rendezVousId));
    }

    @Operation(summary = "Consultations d'un médecin")
    @GetMapping("/medecin/{medecinId}")
    public ResponseEntity<List<ConsultationResponse>> getByMedecin(
            @Parameter(description = "ID du médecin", required = true, example = "1")
            @PathVariable Long medecinId) {
        return ResponseEntity.ok(consultationService.getConsultationsByMedecin(medecinId));
    }

    @Operation(summary = "Consultations d'un patient")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ConsultationResponse>> getByPatient(
            @Parameter(description = "ID du patient", required = true, example = "3")
            @PathVariable Long patientId) {
        return ResponseEntity.ok(consultationService.getConsultationsByPatient(patientId));
    }

    // ==================== FACTURES ====================

    @Operation(
        summary = "Générer une facture",
        description = "Génère la facture d'une consultation (total = prix consultation + médicaments)."
    )
    @ApiResponse(responseCode = "200", description = "Facture générée",
        content = @Content(schema = @Schema(implementation = FactureResponse.class),
            examples = @ExampleObject(value = """
                {
                  "consultationId": 5,
                  "patientNom": "Jean Dupont",
                  "medecinNom": "Dr. Sophie Martin",
                  "dateConsultation": "2026-06-01",
                  "prixConsultation": 60.0,
                  "montantMedicaments": 25.0,
                  "total": 85.0,
                  "statutPaiement": "EN_ATTENTE"
                }""")))
    @GetMapping("/{id}/facture")
    public ResponseEntity<FactureResponse> genererFacture(
            @Parameter(description = "ID de la consultation", required = true, example = "5")
            @PathVariable Long id) {
        logger.info("📄 Génération facture pour consultation: {}", id);
        return ResponseEntity.ok(consultationService.genererFacture(id));
    }

    // Le patient peut voir ses propres factures
    @Operation(summary = "Factures d'un patient", description = "Retourne toutes les factures d'un patient.")
    @PreAuthorize("hasAnyRole('PATIENT', 'MEDECIN', 'ADMIN')")
    @GetMapping("/patient/{patientId}/factures")
    public ResponseEntity<List<FactureResponse>> getFacturesPatient(
            @Parameter(description = "ID du patient", required = true, example = "3")
            @PathVariable Long patientId) {
        return ResponseEntity.ok(consultationService.getFacturesByPatient(patientId));
    }

    @Operation(
        summary = "Mettre à jour le statut de paiement",
        description = "Met à jour le statut de paiement d'une consultation. Valeurs possibles : `PAYE`, `EN_ATTENTE`, `ANNULE`."
    )
    @PutMapping("/{id}/paiement")
    public ResponseEntity<FactureResponse> updatePaiement(
            @Parameter(description = "ID de la consultation", required = true, example = "5")
            @PathVariable Long id,
            @Parameter(description = "Nouveau statut de paiement", required = true, example = "PAYE",
                schema = @Schema(allowableValues = {"PAYE", "EN_ATTENTE", "ANNULE"}))
            @RequestParam String statut) {
        logger.info("💳 Mise à jour paiement: {} -> {}", id, statut);
        return ResponseEntity.ok(consultationService.updateStatutPaiement(id, statut));
    }

    @Operation(summary = "Revenus d'un médecin", description = "Calcule le total des revenus d'un médecin.")
    @GetMapping("/medecin/{medecinId}/revenus")
    public ResponseEntity<Double> getRevenusMedecin(
            @Parameter(description = "ID du médecin", required = true, example = "1")
            @PathVariable Long medecinId) {
        return ResponseEntity.ok(consultationService.calculerRevenusMedecin(medecinId));
    }

    @Operation(
        summary = "Générer facture PDF",
        description = "Génère la version PDF de la facture après paiement."
    )
    @GetMapping("/{id}/facture/pdf")
    public ResponseEntity<FactureResponse> genererFacturePDF(
            @Parameter(description = "ID de la consultation", required = true, example = "5")
            @PathVariable Long id) {
        return ResponseEntity.ok(consultationService.genererFacturePDF(id));
    }

    @Operation(
        summary = "Statistiques de facturation",
        description = "Retourne les statistiques de facturation d'un médecin sur une période donnée (nombre de consultations, revenus totaux, etc.)."
    )
    @GetMapping("/medecin/{medecinId}/statistiques")
    public ResponseEntity<Map<String, Object>> getStatistiques(
            @Parameter(description = "ID du médecin", required = true, example = "1")
            @PathVariable Long medecinId,
            @Parameter(description = "Date de début (yyyy-MM-dd)", required = true, example = "2026-01-01")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date debut,
            @Parameter(description = "Date de fin (yyyy-MM-dd)", required = true, example = "2026-12-31")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fin) {
        return ResponseEntity.ok(consultationService.getStatistiquesFacturation(medecinId, debut, fin));
    }
}
