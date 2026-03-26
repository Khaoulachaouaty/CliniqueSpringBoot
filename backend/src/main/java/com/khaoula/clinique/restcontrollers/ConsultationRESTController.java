package com.khaoula.clinique.restcontrollers;


import com.khaoula.clinique.entities.Consultation;
import com.khaoula.clinique.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultations")
@CrossOrigin(origins = "*")
public class ConsultationRESTController {
    
    @Autowired
    private ConsultationService consultationService;
    
    @PostMapping("/save")
    public Consultation saveConsultation(@RequestBody Consultation consultation) {
        return consultationService.saveConsultation(consultation);
    }
    
    @GetMapping("/all")
    public List<Consultation> getAllConsultations() {
        return consultationService.getAllConsultations();
    }
    
    @GetMapping("/{id}")
    public Consultation getConsultationById(@PathVariable Long id) {
        return consultationService.getConsultationById(id);
    }
    
    @GetMapping("/rendezvous/{rendezVousId}")
    public Consultation getConsultationByRendezVous(@PathVariable Long rendezVousId) {
        return consultationService.getConsultationByRendezVous(rendezVousId);
    }
    
    @GetMapping("/facture/{id}")
    public double getTotalFacture(@PathVariable Long id) {
        return consultationService.getTotalFacture(id);
    }
}