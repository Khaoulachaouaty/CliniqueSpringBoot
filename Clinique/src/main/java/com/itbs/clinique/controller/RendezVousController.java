package com.itbs.clinique.controller;

import com.itbs.clinique.dto.*;
import com.itbs.clinique.services.RendezVousService;

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

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/rendezvous")
@CrossOrigin(origins = "*")
@Tag(name = "Rendez-vous", description = "Prise, modification et annulation de rendez-vous")
@SecurityRequirement(name = "bearerAuth")
public class RendezVousController {

    private static final Logger logger = LoggerFactory.getLogger(RendezVousController.class);

    private final RendezVousService rendezVousService;

    public RendezVousController(RendezVousService rendezVousService) {
        this.rendezVousService = rendezVousService;
    }

    // ==================== PATIENT ====================

    @Operation(
        summary = "Prendre un rendez-vous",
        description = "Crée un nouveau rendez-vous pour un patient avec un médecin à une date et heure données."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Rendez-vous créé",
            content = @Content(schema = @Schema(implementation = RendezVousResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "id": 12,
                      "patientId": 3,
                      "medecinId": 1,
                      "date": "2026-06-01",
                      "heure": "09:00",
                      "motif": "Douleurs thoraciques",
                      "statut": "EN_ATTENTE"
                    }"""))),
        @ApiResponse(responseCode = "400", description = "Créneau déjà occupé ou données invalides", content = @Content)
    })
    @PostMapping
    public ResponseEntity<RendezVousResponse> createRendezVous(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Détails du rendez-vous",
                required = true,
                content = @Content(examples = @ExampleObject(value = """
                    {
                      "patientId": 3,
                      "medecinId": 1,
                      "date": "2026-06-01",
                      "heure": "09:00",
                      "motif": "Douleurs thoraciques"
                    }""")))
            @Valid @RequestBody RendezVousRequest request) {
        logger.info("📅 Création RDV: patient={}, medecin={}, date={}",
                request.getPatientId(), request.getMedecinId(), request.getDate());
        return ResponseEntity.ok(rendezVousService.createRendezVous(request));
    }

    @Operation(summary = "Rendez-vous d'un patient", description = "Retourne tous les rendez-vous d'un patient.")
    @ApiResponse(responseCode = "200", description = "Liste des rendez-vous du patient")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<RendezVousResponse>> getRendezVousByPatient(
            @Parameter(description = "ID du patient", required = true, example = "3")
            @PathVariable Long patientId) {
        logger.info("📋 Liste RDV patient: {}", patientId);
        return ResponseEntity.ok(rendezVousService.getRendezVousByPatient(patientId));
    }

    @Operation(
        summary = "Annuler un rendez-vous",
        description = "Annule un rendez-vous. Seul le patient propriétaire peut annuler."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Rendez-vous annulé",
            content = @Content(schema = @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "403", description = "Non autorisé", content = @Content),
        @ApiResponse(responseCode = "404", description = "Rendez-vous introuvable", content = @Content)
    })
    @PutMapping("/{id}/cancel")
    public ResponseEntity<MessageResponse> cancelRendezVous(
            @Parameter(description = "ID du rendez-vous", required = true, example = "12")
            @PathVariable Long id,
            @Parameter(description = "ID du patient", required = true, example = "3")
            @RequestParam Long patientId) {
        logger.info("❌ Annulation RDV: {} par patient: {}", id, patientId);
        rendezVousService.cancelRendezVous(id, patientId);
        return ResponseEntity.ok(new MessageResponse("Rendez-vous annulé avec succès", true));
    }

    @Operation(
        summary = "Modifier un rendez-vous",
        description = "Modifie la date, l'heure ou le motif d'un rendez-vous existant."
    )
    @PutMapping("/{id}")
    public ResponseEntity<RendezVousResponse> modifierRendezVous(
            @Parameter(description = "ID du rendez-vous", required = true, example = "12")
            @PathVariable Long id,
            @Valid @RequestBody RendezVousRequest request,
            @Parameter(description = "ID du patient", required = true, example = "3")
            @RequestParam Long patientId) {
        logger.info("✏️ Modification RDV: {} par patient: {}", id, patientId);
        return ResponseEntity.ok(rendezVousService.modifierRendezVous(id, request, patientId));
    }

    // ==================== MEDECIN ====================

    @Operation(summary = "Rendez-vous d'un médecin", description = "Retourne tous les rendez-vous d'un médecin.")
    @ApiResponse(responseCode = "200", description = "Liste des rendez-vous du médecin")
    @GetMapping("/medecin/{medecinId}")
    public ResponseEntity<List<RendezVousResponse>> getRendezVousByMedecin(
            @Parameter(description = "ID du médecin", required = true, example = "1")
            @PathVariable Long medecinId) {
        logger.info("📋 Liste RDV médecin: {}", medecinId);
        return ResponseEntity.ok(rendezVousService.getRendezVousByMedecin(medecinId));
    }

    @Operation(
        summary = "Rendez-vous du jour d'un médecin",
        description = "Retourne les rendez-vous d'un médecin pour une date donnée."
    )
    @GetMapping("/medecin/{medecinId}/today")
    public ResponseEntity<List<RendezVousResponse>> getRendezVousDuJour(
            @Parameter(description = "ID du médecin", required = true, example = "1")
            @PathVariable Long medecinId,
            @Parameter(description = "Date au format yyyy-MM-dd", required = true, example = "2026-06-01")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        logger.info("📅 RDV du jour médecin: {} date: {}", medecinId, date);
        return ResponseEntity.ok(rendezVousService.getRendezVousDuJour(medecinId, date));
    }

    @Operation(
        summary = "Mettre à jour le statut d'un rendez-vous",
        description = "Permet au médecin de confirmer ou refuser un rendez-vous. Statuts possibles : `CONFIRME`, `REFUSE`, `EN_ATTENTE`."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Statut mis à jour",
            content = @Content(schema = @Schema(implementation = RendezVousResponse.class))),
        @ApiResponse(responseCode = "400", description = "Statut manquant ou invalide", content = @Content)
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<RendezVousResponse> updateStatus(
            @Parameter(description = "ID du rendez-vous", required = true, example = "12")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Nouveau statut",
                required = true,
                content = @Content(examples = @ExampleObject(value = """
                    { "statut": "CONFIRME" }""")))
            @RequestBody ConfirmRendezVousRequest request,
            @Parameter(description = "ID du médecin", required = true, example = "1")
            @RequestParam Long medecinId) {
        logger.info("🔍 Requête reçue: id={}, medecinId={}, request={}", id, medecinId, request);
        logger.info("🔍 Statut dans request: {}", request.getStatut());
        if (request.getStatut() == null) {
            logger.error("❌ Statut est NULL !");
            throw new RuntimeException("Le statut est obligatoire");
        }
        return ResponseEntity.ok(rendezVousService.updateStatus(id, request.getStatut(), medecinId));
    }

    @Operation(
        summary = "Calendrier d'un médecin",
        description = "Retourne les événements du calendrier d'un médecin entre deux dates."
    )
    @GetMapping("/medecin/{medecinId}/calendar")
    public ResponseEntity<List<CalendarEventResponse>> getCalendarEvents(
            @Parameter(description = "ID du médecin", required = true, example = "1")
            @PathVariable Long medecinId,
            @Parameter(description = "Date de début (yyyy-MM-dd)", required = true, example = "2026-06-01")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
            @Parameter(description = "Date de fin (yyyy-MM-dd)", required = true, example = "2026-06-30")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) {
        logger.info("📆 Calendrier médecin: {} du {} au {}", medecinId, start, end);
        return ResponseEntity.ok(rendezVousService.getCalendarEvents(medecinId, start, end));
    }

    // ==================== CRÉNEAUX ====================

    @Operation(
        summary = "Créneaux disponibles",
        description = "Retourne la liste des créneaux horaires disponibles pour un médecin à une date donnée."
    )
    @ApiResponse(responseCode = "200", description = "Liste des créneaux disponibles",
        content = @Content(examples = @ExampleObject(value = """
            ["08:00", "08:30", "09:30", "10:00", "14:00", "15:30"]""")))
    @GetMapping("/creneaux/{medecinId}")
    public ResponseEntity<List<String>> getCreneauxDisponibles(
            @Parameter(description = "ID du médecin", required = true, example = "1")
            @PathVariable Long medecinId,
            @Parameter(description = "Date au format yyyy-MM-dd", required = true, example = "2026-06-01")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        logger.info("⏰ Créneaux médecin: {} date: {}", medecinId, date);
        return ResponseEntity.ok(rendezVousService.getCreneauxDisponibles(medecinId, date));
    }

    @Operation(
        summary = "Créneaux occupés",
        description = "Retourne la liste des créneaux déjà réservés pour un médecin à une date donnée."
    )
    @GetMapping("/creneaux/{medecinId}/occupes")
    public ResponseEntity<List<String>> getCreneauxOccupes(
            @Parameter(description = "ID du médecin", required = true, example = "1")
            @PathVariable Long medecinId,
            @Parameter(description = "Date au format yyyy-MM-dd", required = true, example = "2026-06-01")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        logger.info("🔴 Créneaux occupés médecin: {} date: {}", medecinId, date);
        return ResponseEntity.ok(rendezVousService.getCreneauxOccupes(medecinId, date));
    }

    // ==================== ADMIN ====================

    @Operation(summary = "Tous les rendez-vous", description = "Retourne l'ensemble des rendez-vous (accès admin).")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<RendezVousResponse>> getAllRendezVous() {
        logger.info("📋 Liste tous les RDV");
        return ResponseEntity.ok(rendezVousService.getAllRendezVous());
    }

    @Operation(summary = "Filtrer par patient")
    @GetMapping("/filter/patient/{patientId}")
    public ResponseEntity<List<RendezVousResponse>> filterByPatient(
            @Parameter(description = "ID du patient", required = true, example = "3")
            @PathVariable Long patientId) {
        return ResponseEntity.ok(rendezVousService.filterByPatient(patientId));
    }

    @Operation(summary = "Filtrer par spécialité")
    @GetMapping("/filter/specialite/{specialite}")
    public ResponseEntity<List<RendezVousResponse>> filterBySpecialite(
            @Parameter(description = "Spécialité médicale", required = true, example = "Cardiologie")
            @PathVariable String specialite) {
        return ResponseEntity.ok(rendezVousService.filterBySpecialite(specialite));
    }

    @Operation(summary = "Filtrer par médecin et date")
    @GetMapping("/filter/medecin/{medecinId}/date")
    public ResponseEntity<List<RendezVousResponse>> filterByMedecinAndDate(
            @Parameter(description = "ID du médecin", required = true, example = "1")
            @PathVariable Long medecinId,
            @Parameter(description = "Date au format yyyy-MM-dd", required = true, example = "2026-06-01")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        return ResponseEntity.ok(rendezVousService.filterByMedecinAndDate(medecinId, date));
    }
}
