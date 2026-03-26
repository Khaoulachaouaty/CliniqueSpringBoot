package com.khaoula.clinique.restcontrollers;


import com.khaoula.clinique.entities.RendezVous;
import com.khaoula.clinique.service.RendezVousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/rendezvous")
@CrossOrigin(origins = "*")
public class RendezVousRESTController {
    
    @Autowired
    private RendezVousService rendezVousService;
    
    @PostMapping("/save")
    public RendezVous saveRendezVous(@RequestBody RendezVous rendezVous) {
        return rendezVousService.saveRendezVous(rendezVous);
    }
    
    @GetMapping("/all")
    public List<RendezVous> getAllRendezVous() {
        return rendezVousService.getAllRendezVous();
    }
    
    @GetMapping("/{id}")
    public RendezVous getRendezVousById(@PathVariable Long id) {
        return rendezVousService.getRendezVousById(id);
    }
    
    @GetMapping("/patient/{patientId}")
    public List<RendezVous> getRendezVousByPatient(@PathVariable Long patientId) {
        return rendezVousService.getRendezVousByPatient(patientId);
    }
    
    @GetMapping("/medecin/{medecinId}")
    public List<RendezVous> getRendezVousByMedecin(@PathVariable Long medecinId) {
        return rendezVousService.getRendezVousByMedecin(medecinId);
    }
    
    @GetMapping("/medecin/{medecinId}/date/{date}")
    public List<RendezVous> getRendezVousByMedecinAndDate(
            @PathVariable Long medecinId,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        return rendezVousService.getRendezVousByMedecinAndDate(medecinId, date);
    }
    
    @PutMapping("/accepter/{id}")
    public RendezVous accepterRendezVous(@PathVariable Long id) {
        return rendezVousService.accepterRendezVous(id);
    }
    
    @PutMapping("/refuser/{id}")
    public RendezVous refuserRendezVous(@PathVariable Long id) {
        return rendezVousService.refuserRendezVous(id);
    }
    
    @GetMapping("/verifier-disponibilite")
    public boolean verifierDisponibilite(
            @RequestParam Long medecinId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam String heure) {
        return rendezVousService.verifierDisponibilite(medecinId, date, heure);
    }
    
    @DeleteMapping("/delete/{id}")
    public void deleteRendezVous(@PathVariable Long id) {
        rendezVousService.deleteRendezVous(id);
    }
}