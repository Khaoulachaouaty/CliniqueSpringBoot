package com.khaoula.clinique.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
public class Notification {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
    private String message;
    private Date dateEnvoi;
    private String type;        // EMAIL, SMS, PUSH
    private String statut;      // ENVOYE, EN_ATTENTE, ERREUR
    private String destinataire; // email ou tel
    
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;    // notification pour qui
    
    @ManyToOne
    @JoinColumn(name = "rendez_vous_id")
    private RendezVous rendezVous; // lié à quel RDV
        
	public Notification() {
		super();
	}

	public Notification(String message, Date dateEnvoi, String type, String statut, 
	                    String destinataire, Patient patient, RendezVous rendezVous) {
		super();
		this.message = message;
		this.dateEnvoi = dateEnvoi;
		this.type = type;
		this.statut = statut;
		this.destinataire = destinataire;
		this.patient = patient;
		this.rendezVous = rendezVous;
	}

	// Getters & Setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getDateEnvoi() {
		return dateEnvoi;
	}
	public void setDateEnvoi(Date dateEnvoi) {
		this.dateEnvoi = dateEnvoi;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatut() {
		return statut;
	}
	public void setStatut(String statut) {
		this.statut = statut;
	}
	public String getDestinataire() {
		return destinataire;
	}
	public void setDestinataire(String destinataire) {
		this.destinataire = destinataire;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public RendezVous getRendezVous() {
		return rendezVous;
	}
	public void setRendezVous(RendezVous rendezVous) {
		this.rendezVous = rendezVous;
	}

	@Override
	public String toString() {
		return "Notification [id=" + id + ", message=" + message + ", dateEnvoi=" + dateEnvoi 
		    + ", type=" + type + ", statut=" + statut + ", destinataire=" + destinataire + "]";
	}
}