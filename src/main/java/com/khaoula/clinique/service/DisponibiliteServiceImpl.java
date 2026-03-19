package com.khaoula.clinique.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.khaoula.clinique.entities.Disponibilite;
import com.khaoula.clinique.repos.DisponibiliteRepository;

@Service
public class DisponibiliteServiceImpl implements DisponibiliteService {
	
	private final DisponibiliteRepository disponibiliteRepository;
	
	public DisponibiliteServiceImpl(DisponibiliteRepository disponibiliteRepository) {
		super();
		this.disponibiliteRepository = disponibiliteRepository;
	}

	@Override
	public Disponibilite saveDisponibilite(Disponibilite d) {
		return disponibiliteRepository.save(d);
	}
	
	@Override
	public Disponibilite updateDisponibilite(Disponibilite d) {
		return disponibiliteRepository.save(d);
	}
	
	@Override
	public void deleteDisponibilite(Disponibilite d) {
		disponibiliteRepository.delete(d);
	}
	
	@Override
	public void deleteDisponibiliteById(int id) {
		disponibiliteRepository.deleteById(id);
	}
	
	@Override
	public Optional<Disponibilite> getDisponibilite(int id) {
		return disponibiliteRepository.findById(id);
	}
	
	@Override
	public List<Disponibilite> getAllDisponibilites() {
		return disponibiliteRepository.findAll();
	}
	
	@Override
	public List<Disponibilite> findByMedecinId(int medecinId) {
		return disponibiliteRepository.findByMedecinId(medecinId);
	}
	
	@Override
	public List<Disponibilite> findByMedecinAndJour(int medecinId, String jour) {
		return disponibiliteRepository.findByMedecinIdAndJourSemaine(medecinId, jour);
	}
}