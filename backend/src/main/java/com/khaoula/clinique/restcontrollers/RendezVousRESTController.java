package com.khaoula.clinique.restcontrollers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("/api/rendezvous")
public class RendezVousRESTController {

    @Autowired
    private RendezVousService rendezVousService;

    @GetMapping
    public List<RendezVous> getAllRendezVous() {
        return rendezVousService.getAllRendezVous();
    }

    @GetMapping("/{id}")
    public RendezVous getRendezVousById(@PathVariable Long id) {
        return rendezVousService.getRendezVous(id).orElse(null);
    }

    @PostMapping
    public RendezVous createRendezVous(@RequestBody RendezVous rendezVous) {
        return rendezVousService.saveRendezVous(rendezVous);
    }

    @PutMapping("/{id}")
    public RendezVous updateRendezVous(@PathVariable Long id, @RequestBody RendezVous rendezVous) {
        rendezVous.setId(id);
        return rendezVousService.updateRendezVous(rendezVous);
    }

    @DeleteMapping("/{id}")
    public void deleteRendezVous(@PathVariable Long id) {
        rendezVousService.deleteRendezVousById(id);
    }

    @GetMapping("/patient/{patientId}")
    public List<RendezVous> getRendezVousByPatient(@PathVariable Long patientId) {
        return rendezVousService.findByPatientId(patientId);
    }

    @GetMapping("/medecin/{medecinId}")
    public List<RendezVous> getRendezVousByMedecin(@PathVariable Long medecinId) {
        return rendezVousService.findByMedecinId(medecinId);
    }

    @GetMapping("/statut/{statut}")
    public List<RendezVous> getRendezVousByStatut(@PathVariable String statut) {
        return rendezVousService.findByStatut(statut);
    }
    
    @PostMapping("/verifier-disponibilite")
    public ResponseEntity<?> verifierDisponibilite(@RequestBody RendezVous rdv) {
    	boolean disponible = rendezVousService.verifierDisponibilite(rdv);
    	return ResponseEntity.ok(disponible);
    }
}