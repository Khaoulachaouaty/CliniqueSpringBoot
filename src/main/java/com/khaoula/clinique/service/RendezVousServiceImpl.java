package com.khaoula.clinique.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.khaoula.clinique.entities.RendezVous;
import com.khaoula.clinique.repos.RendezVousRepository;

@Service
public class RendezVousServiceImpl implements RendezVousService {
	
	public RendezVousServiceImpl(RendezVousRepository rendezVousRepository) {
		super();
		this.rendezVousRepository = rendezVousRepository;
	}

	private final RendezVousRepository rendezVousRepository;
	
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
	public void deleteRendezVousById(int id) {
		rendezVousRepository.deleteById(id);
	}
	
	@Override
	public Optional<RendezVous> getRendezVous(int id) {
		return rendezVousRepository.findById(id);
	}
	
	@Override
	public List<RendezVous> getAllRendezVous() {
		return rendezVousRepository.findAll();
	}
	
	@Override
	public List<RendezVous> findByPatientId(int patientId) {
		return rendezVousRepository.findByPatientId(patientId);
	}
	
	@Override
	public List<RendezVous> findByMedecinId(int medecinId) {
		return rendezVousRepository.findByMedecinId(medecinId);
	}
	
	@Override
	public List<RendezVous> findByStatut(String statut) {
		return rendezVousRepository.findByStatut(statut);
	}
	
	@Override
	public List<RendezVous> findByMedecinAndStatut(int medecinId, String statut) {
		return rendezVousRepository.findByMedecinIdAndStatut(medecinId, statut);
	}
	
	@Override
	public List<RendezVous> findByPatientAndStatut(int patientId, String statut) {
		return rendezVousRepository.findByPatientIdAndStatut(patientId, statut);
	}
}