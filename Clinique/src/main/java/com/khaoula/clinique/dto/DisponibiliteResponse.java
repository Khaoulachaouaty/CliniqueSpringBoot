package com.khaoula.clinique.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DisponibiliteResponse {
    private Long id;
    private String jourSemaine;
    private String heureDebut;
    private String heureFin;
    private Long medecinId;
    private String medecinNom;
}