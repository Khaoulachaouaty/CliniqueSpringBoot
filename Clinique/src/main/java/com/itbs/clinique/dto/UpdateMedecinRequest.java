package com.itbs.clinique.dto;

import lombok.Data;

@Data
public class UpdateMedecinRequest {
    private String nom;
    private String prenom;
    private String tel;
    private String specialite;
}
