package com.khaoula.clinique.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.khaoula.clinique.entities.Medecin;
import com.khaoula.clinique.repos.MedecinRepository;

@Service
public class MedecinServiceImpl implements MedecinService {
	
	private final MedecinRepository medecinRepository;
	
	public MedecinServiceImpl(MedecinRepository medecinRepository) {
		super();
		this.medecinRepository = medecinRepository;
	}

	@Override
	public Medecin saveMedecin(Medecin m) {
		return medecinRepository.save(m);
	}
	
	@Override
	public Medecin updateMedecin(Medecin m) {
		return medecinRepository.save(m);
	}
	
	@Override
	public void deleteMedecin(Medecin m) {
		medecinRepository.delete(m);
	}
	
	@Override
	public void deleteMedecinById(int id) {
		medecinRepository.deleteById(id);
	}
	
	@Override
	public Optional<Medecin> getMedecin(int id) {
		return medecinRepository.findById(id);
	}
	
	@Override
	public List<Medecin> getAllMedecins() {
		return medecinRepository.findAll();
	}
	
	@Override
	public List<Medecin> findBySpecialite(String specialite) {
		return medecinRepository.findBySpecialite(specialite);
	}
	
	@Override
	public Optional<Medecin> findByEmail(String email) {
		return medecinRepository.findByEmail(email);
	}
}