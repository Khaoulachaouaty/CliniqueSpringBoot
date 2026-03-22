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

import com.khaoula.clinique.entities.Patient;
import com.khaoula.clinique.service.PatientService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
@AllArgsConstructor

public class PatientRESTController {

	//@Autowired
	private final PatientService patientService;

	@GetMapping
	public List<Patient> getAllPatients() {
		return patientService.getAllPatients();
	}

	@GetMapping("/{id}")
	public Patient getPatientById(@PathVariable int id) {
		return patientService.getPatient(id).orElse(null);
	}

	@PostMapping
	public Patient createPatient(@RequestBody Patient patient) {
		return patientService.savePatient(patient);
	}

	@PutMapping("/{id}")
	public Patient updatePatient(@PathVariable int id, @RequestBody Patient patient) {
		patient.setId(id);
		return patientService.updatePatient(patient);
	}

	@DeleteMapping("/{id}")
	public void deletePatient(@PathVariable int id) {
		patientService.deletePatientById(id);
	}
	
	@GetMapping("/email/{email}")
	public Patient findByEmail(@PathVariable String email) {
		return patientService.findByEmail(email).orElse(null);
	}
}