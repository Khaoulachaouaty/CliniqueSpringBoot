package com.khaoula.clinique.restcontrollers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.khaoula.clinique.entities.Consultation;
import com.khaoula.clinique.service.ConsultationService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/consultations")
@CrossOrigin(origins = "*")
@AllArgsConstructor

public class ConsultationRESTController {

	//@Autowired
	private final ConsultationService consultationService;

	@GetMapping
	public List<Consultation> getAllConsultations() {
		return consultationService.getAllConsultations();
	}

	@GetMapping("/{id}")
	public Consultation getConsultationById(@PathVariable int id) {
		return consultationService.getConsultation(id).orElse(null);
	}

	@PostMapping
	public Consultation createConsultation(@RequestBody Consultation consultation) {
		return consultationService.saveConsultation(consultation);
	}

	@PutMapping("/{id}")
	public Consultation updateConsultation(@PathVariable int id, @RequestBody Consultation consultation) {
		consultation.setId(id);
		return consultationService.updateConsultation(consultation);
	}

	@DeleteMapping("/{id}")
	public void deleteConsultation(@PathVariable int id) {
		consultationService.deleteConsultationById(id);
	}
	
	@GetMapping("/rendezvous/{rendezVousId}")
	public Consultation findByRendezVous(@PathVariable int rendezVousId) {
		return consultationService.findByRendezVousId(rendezVousId).orElse(null);
	}
}