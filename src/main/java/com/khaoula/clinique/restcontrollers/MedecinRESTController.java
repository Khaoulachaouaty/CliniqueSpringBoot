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

import com.khaoula.clinique.entities.Medecin;
import com.khaoula.clinique.service.MedecinService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/medecins")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class MedecinRESTController {

	//@Autowired
	private final MedecinService medecinService;

	@GetMapping
	public List<Medecin> getAllMedecins() {
		return medecinService.getAllMedecins();
	}

	@GetMapping("/{id}")
	public Medecin getMedecinById(@PathVariable int id) {
		return medecinService.getMedecin(id).orElse(null);
	}

	@PostMapping
	public Medecin createMedecin(@RequestBody Medecin medecin) {
		return medecinService.saveMedecin(medecin);
	}

	@PutMapping("/{id}")
	public Medecin updateMedecin(@PathVariable int id, @RequestBody Medecin medecin) {
		medecin.setId(id);
		return medecinService.updateMedecin(medecin);
	}

	@DeleteMapping("/{id}")
	public void deleteMedecin(@PathVariable int id) {
		medecinService.deleteMedecinById(id);
	}
	
	@GetMapping("/specialite/{specialite}")
	public List<Medecin> findBySpecialite(@PathVariable String specialite) {
		return medecinService.findBySpecialite(specialite);
	}
}