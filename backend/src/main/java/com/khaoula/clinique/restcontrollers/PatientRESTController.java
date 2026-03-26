package com.khaoula.clinique.restcontrollers;


import com.khaoula.clinique.entities.Patient;
import com.khaoula.clinique.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientRESTController {
    
    @Autowired
    private PatientService patientService;
    
    @PostMapping("/save")
    public Patient savePatient(@RequestBody Patient patient) {
        return patientService.savePatient(patient);
    }
    
    @GetMapping("/all")
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }
    
    @GetMapping("/{id}")
    public Patient getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }
    
    @GetMapping("/find")
    public Patient getPatientByUsername(@RequestParam String username) {
        return patientService.getPatientByUsername(username);
    }
    
    @DeleteMapping("/delete/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }
}