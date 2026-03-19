package com.khaoula.clinique.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
public class RendezVous {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private Date date;
	private String heure;
	private String motif;
	private String statut; // EN_ATTENTE, CONFIRME, TERMINE, ANNULE
	
	@ManyToOne
	@JoinColumn(name = "patient_id")
	private Patient patient;
	
	@ManyToOne
	@JoinColumn(name = "medecin_id")
	private Medecin medecin;
        
	public RendezVous() {
		super();
	}

	public RendezVous(Date date, String heure, String motif, String statut, Patient patient, Medecin medecin) {
		super();
		this.date = date;
		this.heure = heure;
		this.motif = motif;
		this.statut = statut;
		this.patient = patient;
		this.medecin = medecin;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getHeure() {
		return heure;
	}
	public void setHeure(String heure) {
		this.heure = heure;
	}
	public String getMotif() {
		return motif;
	}
	public void setMotif(String motif) {
		this.motif = motif;
	}
	public String getStatut() {
		return statut;
	}
	public void setStatut(String statut) {
		this.statut = statut;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public Medecin getMedecin() {
		return medecin;
	}
	public void setMedecin(Medecin medecin) {
		this.medecin = medecin;
	}

	@Override
	public String toString() {
		return "RendezVous [id=" + id + ", date=" + date + ", heure=" + heure + ", motif=" + motif + ", statut="
				+ statut + ", patient=" + patient + ", medecin=" + medecin + "]";
	}
}