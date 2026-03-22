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

import com.khaoula.clinique.entities.RendezVous;
import com.khaoula.clinique.service.RendezVousService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/rendezvous")
@CrossOrigin(origins = "*")
@AllArgsConstructor

public class RendezVousRESTController {

	//@Autowired
	private final RendezVousService rendezVousService;

	@GetMapping
	public List<RendezVous> getAllRendezVous() {
		return rendezVousService.getAllRendezVous();
	}

	@GetMapping("/{id}")
	public RendezVous getRendezVousById(@PathVariable int id) {
		return rendezVousService.getRendezVous(id).orElse(null);
	}

	@PostMapping
	public RendezVous createRendezVous(@RequestBody RendezVous rendezVous) {
		return rendezVousService.saveRendezVous(rendezVous);
	}

	@PutMapping("/{id}")
	public RendezVous updateRendezVous(@PathVariable int id, @RequestBody RendezVous rendezVous) {
		rendezVous.setId(id);
		return rendezVousService.updateRendezVous(rendezVous);
	}

	@DeleteMapping("/{id}")
	public void deleteRendezVous(@PathVariable int id) {
		rendezVousService.deleteRendezVousById(id);
	}
	
	@GetMapping("/patient/{patientId}")
	public List<RendezVous> findByPatient(@PathVariable int patientId) {
		return rendezVousService.findByPatientId(patientId);
	}
	
	@GetMapping("/medecin/{medecinId}")
	public List<RendezVous> findByMedecin(@PathVariable int medecinId) {
		return rendezVousService.findByMedecinId(medecinId);
	}
	
	@GetMapping("/statut/{statut}")
	public List<RendezVous> findByStatut(@PathVariable String statut) {
		return rendezVousService.findByStatut(statut);
	}
}