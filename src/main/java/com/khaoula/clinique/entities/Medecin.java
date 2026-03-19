package com.khaoula.clinique.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
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
	
	public Medecin() {
		super();
	}

	public Medecin(String nom, String email, String password, String specialite) {
		super();
		this.nom = nom;
		this.email = email;
		this.password = password;
		this.specialite = specialite;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSpecialite() {
		return specialite;
	}
	public void setSpecialite(String specialite) {
		this.specialite = specialite;
	}
	public List<Disponibilite> getDisponibilites() {
		return disponibilites;
	}
	public void setDisponibilites(List<Disponibilite> disponibilites) {
		this.disponibilites = disponibilites;
	}
	public List<RendezVous> getRendezVous() {
		return rendezVous;
	}
	public void setRendezVous(List<RendezVous> rendezVous) {
		this.rendezVous = rendezVous;
	}

	@Override
	public String toString() {
		return "Medecin [id=" + id + ", nom=" + nom + ", email=" + email + ", specialite=" + specialite + "]";
	}
}