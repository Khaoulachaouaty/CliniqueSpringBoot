package com.khaoula.clinique.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medecin {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
    private String nom;
    private String email;
    private String password;
    private String specialite;
    
    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL)
    private List<Disponibilite> disponibilites;
    
    @OneToMany(mappedBy = "medecin")
    private List<RendezVous> rendezVous;
}