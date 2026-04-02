package com.khaoula.clinique.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConfirmRendezVousRequest {
    
    @NotBlank(message = "Le statut est obligatoire")
    private String statut; // CONFIRME, ANNULE, TERMINE
}