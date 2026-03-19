package com.khaoula.clinique.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Disponibilite {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String jourSemaine;      // LUNDI, MARDI, MERCREDI, JEUDI, VENDREDI, SAMEDI, DIMANCHE
	private String heureDebut;       // 09:00
	private String heureFin;         // 12:00
	
	@ManyToOne
	@JoinColumn(name = "medecin_id")
	private Medecin medecin;
	
	public Disponibilite() {
		super();
	}

	public Disponibilite(String jourSemaine, String heureDebut, String heureFin, Medecin medecin) {
		super();
		this.jourSemaine = jourSemaine;
		this.heureDebut = heureDebut;
		this.heureFin = heureFin;
		this.medecin = medecin;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getJourSemaine() {
		return jourSemaine;
	}
	public void setJourSemaine(String jourSemaine) {
		this.jourSemaine = jourSemaine;
	}
	public String getHeureDebut() {
		return heureDebut;
	}
	public void setHeureDebut(String heureDebut) {
		this.heureDebut = heureDebut;
	}
	public String getHeureFin() {
		return heureFin;
	}
	public void setHeureFin(String heureFin) {
		this.heureFin = heureFin;
	}
	public Medecin getMedecin() {
		return medecin;
	}
	public void setMedecin(Medecin medecin) {
		this.medecin = medecin;
	}

	@Override
	public String toString() {
		return "Disponibilite [id=" + id + ", jourSemaine=" + jourSemaine + ", heureDebut=" + heureDebut 
		    + ", heureFin=" + heureFin + "]";
	}
}