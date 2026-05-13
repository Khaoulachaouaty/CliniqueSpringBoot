package com.itbs.clinique.controller;

import com.itbs.clinique.dto.*;
import com.itbs.clinique.services.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
@Tag(name = "Notifications", description = "Gestion des notifications pour patients et médecins")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // ==================== CRÉATION ====================

    @Operation(
        summary = "Créer une notification",
        description = "Envoie une notification à un patient ou un médecin. Types disponibles : `RAPPEL_RDV`, `CONFIRMATION_RDV`, `ANNULATION_RDV`, `INFO`."
    )
    @ApiResponse(responseCode = "200", description = "Notification créée",
        content = @Content(schema = @Schema(implementation = NotificationResponse.class)))
    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Données de la notification",
                required = true,
                content = @Content(examples = @ExampleObject(value = """
                    {
                      "patientId": 3,
                      "medecinId": 1,
                      "rendezVousId": 12,
                      "message": "Rappel : votre rendez-vous est demain à 09h00",
                      "type": "RAPPEL_RDV",
                      "donnees": null
                    }""")))
            @Valid @RequestBody NotificationRequest request) {
        logger.info("🔔 Nouvelle notification pour patient: {}", request.getPatientId());
        return ResponseEntity.ok(notificationService.createNotification(request));
    }

    // ==================== PATIENT ====================

    @Operation(summary = "Toutes les notifications d'un patient")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<NotificationResponse>> getByPatient(
            @Parameter(description = "ID du patient", required = true, example = "3")
            @PathVariable Long patientId) {
        return ResponseEntity.ok(notificationService.getNotificationsByPatient(patientId));
    }

    @Operation(summary = "Notifications non lues d'un patient")
    @GetMapping("/patient/{patientId}/non-lues")
    public ResponseEntity<List<NotificationResponse>> getNonLuesPatient(
            @Parameter(description = "ID du patient", required = true, example = "3")
            @PathVariable Long patientId) {
        return ResponseEntity.ok(notificationService.getNotificationsNonLues(patientId, "PATIENT"));
    }

    @Operation(summary = "Nombre de notifications non lues (patient)")
    @ApiResponse(responseCode = "200", description = "Nombre de notifications non lues",
        content = @Content(examples = @ExampleObject(value = "3")))
    @GetMapping("/patient/{patientId}/count-non-lues")
    public ResponseEntity<Long> countNonLuesPatient(
            @Parameter(description = "ID du patient", required = true, example = "3")
            @PathVariable Long patientId) {
        return ResponseEntity.ok(notificationService.getNombreNotificationsNonLues(patientId, "PATIENT"));
    }

    @Operation(summary = "Marquer toutes les notifications comme lues (patient)")
    @PutMapping("/patient/{patientId}/tout-lire")
    public ResponseEntity<MessageResponse> marquerToutLuePatient(
            @Parameter(description = "ID du patient", required = true, example = "3")
            @PathVariable Long patientId) {
        notificationService.marquerToutesCommeLues(patientId, "PATIENT");
        return ResponseEntity.ok(new MessageResponse("Toutes les notifications marquées comme lues", true));
    }

    // ==================== MÉDECIN ====================

    @Operation(summary = "Toutes les notifications d'un médecin")
    @GetMapping("/medecin/{medecinId}")
    public ResponseEntity<List<NotificationResponse>> getByMedecin(
            @Parameter(description = "ID du médecin", required = true, example = "1")
            @PathVariable Long medecinId) {
        return ResponseEntity.ok(notificationService.getNotificationsByMedecin(medecinId));
    }

    @Operation(summary = "Notifications non lues d'un médecin")
    @GetMapping("/medecin/{medecinId}/non-lues")
    public ResponseEntity<List<NotificationResponse>> getNonLuesMedecin(
            @Parameter(description = "ID du médecin", required = true, example = "1")
            @PathVariable Long medecinId) {
        return ResponseEntity.ok(notificationService.getNotificationsNonLues(medecinId, "MEDECIN"));
    }

    @Operation(summary = "Nombre de notifications non lues (médecin)")
    @GetMapping("/medecin/{medecinId}/count-non-lues")
    public ResponseEntity<Long> countNonLuesMedecin(
            @Parameter(description = "ID du médecin", required = true, example = "1")
            @PathVariable Long medecinId) {
        return ResponseEntity.ok(notificationService.getNombreNotificationsNonLues(medecinId, "MEDECIN"));
    }

    @Operation(summary = "Marquer toutes les notifications comme lues (médecin)")
    @PutMapping("/medecin/{medecinId}/tout-lire")
    public ResponseEntity<MessageResponse> marquerToutLueMedecin(
            @Parameter(description = "ID du médecin", required = true, example = "1")
            @PathVariable Long medecinId) {
        notificationService.marquerToutesCommeLues(medecinId, "MEDECIN");
        return ResponseEntity.ok(new MessageResponse("Toutes les notifications marquées comme lues", true));
    }

    // ==================== ACTIONS ====================

    @Operation(summary = "Marquer une notification comme lue")
    @PutMapping("/{id}/lue")
    public ResponseEntity<NotificationResponse> marquerLue(
            @Parameter(description = "ID de la notification", required = true, example = "7")
            @PathVariable Long id) {
        logger.info("👁️ Notification marquée comme lue: {}", id);
        return ResponseEntity.ok(notificationService.marquerCommeLue(id));
    }

    // ==================== SIMULATION ====================

    @Operation(
        summary = "Simuler l'envoi de rappels automatiques",
        description = "Déclenche manuellement l'envoi des rappels de rendez-vous (normalement exécuté automatiquement chaque nuit)."
    )
    @PostMapping("/simuler/rappels")
    public ResponseEntity<MessageResponse> simulerRappels() {
        logger.info("🧪 Simulation envoi rappels...");
        notificationService.envoyerRappelsAutomatiques();
        return ResponseEntity.ok(new MessageResponse("Rappels simulés envoyés", true));
    }
}
