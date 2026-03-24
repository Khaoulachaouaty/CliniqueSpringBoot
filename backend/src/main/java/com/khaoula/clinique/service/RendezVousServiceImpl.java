package com.khaoula.clinique.service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khaoula.clinique.entities.Disponibilite;
import com.khaoula.clinique.entities.RendezVous;
import com.khaoula.clinique.repos.DisponibiliteRepository;
import com.khaoula.clinique.repos.RendezVousRepository;

@Service
public class RendezVousServiceImpl implements RendezVousService {

	@Autowired
	private RendezVousRepository rendezVousRepository;
	
	@Autowired
	private DisponibiliteRepository disponibiliteRepository;

	@Override
	public RendezVous saveRendezVous(RendezVous r) {
		return rendezVousRepository.save(r);
	}

	@Override
	public RendezVous updateRendezVous(RendezVous r) {
		return rendezVousRepository.save(r);
	}

	@Override
	public void deleteRendezVous(RendezVous r) {
		rendezVousRepository.delete(r);
	}

	@Override
	public void deleteRendezVousById(Long id) {
		rendezVousRepository.deleteById(id);
	}

	@Override
	public Optional<RendezVous> getRendezVous(Long id) {
		return rendezVousRepository.findById(id);
	}

	@Override
	public List<RendezVous> getAllRendezVous() {
		return rendezVousRepository.findAll();
	}

	@Override
	public List<RendezVous> findByPatientId(Long patientId) {
		return rendezVousRepository.findByPatientId(patientId);
	}

	@Override
	public List<RendezVous> findByMedecinId(Long medecinId) {
		return rendezVousRepository.findByMedecinId(medecinId);
	}

	@Override
	public List<RendezVous> findByStatut(String statut) {
		return rendezVousRepository.findByStatut(statut);
	}

	@Override
	public List<RendezVous> findByMedecinAndStatut(Long medecinId, String statut) {
		return rendezVousRepository.findByMedecinIdAndStatut(medecinId, statut);
	}

	@Override
	public List<RendezVous> findByPatientAndStatut(Long patientId, String statut) {
		return rendezVousRepository.findByPatientIdAndStatut(patientId, statut);
	}

	// ============================================================
	// MÉTHODE DE VÉRIFICATION DISPONIBILITÉ
	// ============================================================
	
	@Override
	public boolean verifierDisponibilite(RendezVous rdv) {
		// 1. Vérifier que le médecin existe
		if (rdv.getMedecin() == null || rdv.getMedecin().getId() == null) {
			return false;
		}
		
		Long medecinId = rdv.getMedecin().getId();
		
		// 2. Récupérer le jour de la semaine
		String jourSemaine = getJourSemaine(rdv.getDate());
		
		// 3. Chercher les disponibilités du médecin ce jour
		// Option A: Avec la méthode Spring Data (si ajoutée)
		List<Disponibilite> dispos = disponibiliteRepository.findByMedecinIdAndJourSemaine(medecinId, jourSemaine);
		
		// Option B: Si la méthode n'existe pas, utiliser celle-ci :
		// List<Disponibilite> dispos = findDisponibilitesByMedecinAndJour(medecinId, jourSemaine);
		
		if (dispos.isEmpty()) {
			System.out.println("❌ Le médecin ne travaille pas ce jour: " + jourSemaine);
			return false;
		}
		
		// 4. Vérifier si l'heure est dans un créneau
		boolean heureValide = dispos.stream().anyMatch(d -> 
			rdv.getHeure().compareTo(d.getHeureDebut()) >= 0 && 
			rdv.getHeure().compareTo(d.getHeureFin()) <= 0
		);
		
		if (!heureValide) {
			System.out.println("❌ Heure " + rdv.getHeure() + " hors créneaux disponibles");
			return false;
		}
		
		// 5. Vérifier si le créneau n'est pas déjà pris
		List<RendezVous> existants = rendezVousRepository.findByMedecinId(medecinId);
		boolean dejaPris = existants.stream().anyMatch(r -> 
			r.getDate() != null &&
			r.getDate().equals(rdv.getDate()) && 
			r.getHeure().equals(rdv.getHeure()) &&
			!r.getStatut().equals("ANNULE") &&
			(rdv.getId() == null || !rdv.getId().equals(r.getId())) // Exclure le RDV en cours de modif
		);
		
		if (dejaPris) {
			System.out.println("❌ Créneau déjà réservé");
			return false;
		}
		
		System.out.println("✅ Créneau disponible");
		return true;
	}
	
	// Méthode alternative si findByMedecinIdAndJourSemaine n'existe pas
	private List<Disponibilite> findDisponibilitesByMedecinAndJour(Long medecinId, String jour) {
		List<Disponibilite> all = disponibiliteRepository.findByMedecinId(medecinId);
		return all.stream()
				  .filter(d -> d.getJourSemaine() != null && 
							   d.getJourSemaine().equalsIgnoreCase(jour))
				  .collect(Collectors.toList());
	}
	
	private String getJourSemaine(java.util.Date date) {
		if (date == null) return "";
		String[] jours = {"DIMANCHE", "LUNDI", "MARDI", "MERCREDI", "JEUDI", "VENDREDI", "SAMEDI"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return jours[cal.get(Calendar.DAY_OF_WEEK) - 1];
	}
}