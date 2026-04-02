package com.khaoula.clinique.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DisponibiliteRequest {
    
    @NotNull(message = "L'ID du médecin est obligatoire")
    private Long medecinId;
    
    @NotBlank(message = "Le jour de la semaine est obligatoire")
    private String jourSemaine; // LUNDI, MARDI, etc.
    
    @NotBlank(message = "L'heure de début est obligatoire")
    private String heureDebut; // format HH:mm
    
    @NotBlank(message = "L'heure de fin est obligatoire")
    private String heureFin; // format HH:mm
}
