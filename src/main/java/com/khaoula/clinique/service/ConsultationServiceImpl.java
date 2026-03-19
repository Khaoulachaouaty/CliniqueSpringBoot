package com.khaoula.clinique.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.khaoula.clinique.entities.Consultation;
import com.khaoula.clinique.repos.ConsultationRepository;

@Service
public class ConsultationServiceImpl implements ConsultationService {
	
	private final ConsultationRepository consultationRepository;
	
	public ConsultationServiceImpl(ConsultationRepository consultationRepository) {
		super();
		this.consultationRepository = consultationRepository;
	}

	@Override
	public Consultation saveConsultation(Consultation c) {
		return consultationRepository.save(c);
	}
	
	@Override
	public Consultation updateConsultation(Consultation c) {
		return consultationRepository.save(c);
	}
	
	@Override
	public void deleteConsultation(Consultation c) {
		consultationRepository.delete(c);
	}
	
	@Override
	public void deleteConsultationById(int id) {
		consultationRepository.deleteById(id);
	}
	
	@Override
	public Optional<Consultation> getConsultation(int id) {
		return consultationRepository.findById(id);
	}
	
	@Override
	public List<Consultation> getAllConsultations() {
		return consultationRepository.findAll();
	}
	
	@Override
	public Optional<Consultation> findByRendezVousId(int rendezVousId) {
		return consultationRepository.findByRendezVousId(rendezVousId);
	}
}