package com.khaoula.clinique.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;

@Entity
public class Consultation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
    private String diagnostic;
    private String ordonnance;
    private double prix;
    
    @OneToOne
    @JoinColumn(name = "rendez_vous_id")
    private RendezVous rendezVous;
        
	public Consultation() {
		super();
	}

	public Consultation(String diagnostic, String ordonnance, double prix, RendezVous rendezVous) {
		super();
		this.diagnostic = diagnostic;
		this.ordonnance = ordonnance;
		this.prix = prix;
		this.rendezVous = rendezVous;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDiagnostic() {
		return diagnostic;
	}
	public void setDiagnostic(String diagnostic) {
		this.diagnostic = diagnostic;
	}
	public String getOrdonnance() {
		return ordonnance;
	}
	public void setOrdonnance(String ordonnance) {
		this.ordonnance = ordonnance;
	}
	public double getPrix() {
		return prix;
	}
	public void setPrix(double prix) {
		this.prix = prix;
	}
	public RendezVous getRendezVous() {
		return rendezVous;
	}
	public void setRendezVous(RendezVous rendezVous) {
		this.rendezVous = rendezVous;
	}

	@Override
	public String toString() {
		return "Consultation [id=" + id + ", diagnostic=" + diagnostic + ", ordonnance=" + ordonnance + ", prix="
				+ prix + ", rendezVous=" + rendezVous + "]";
	}
}