package com.khaoula.clinique.restcontrollers;


import com.khaoula.clinique.entities.Medecin;
import com.khaoula.clinique.service.MedecinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medecins")
@CrossOrigin(origins = "*")
public class MedecinRESTController {
    
    @Autowired
    private MedecinService medecinService;
    
    @PostMapping("/save")
    public Medecin saveMedecin(@RequestBody Medecin medecin) {
        return medecinService.saveMedecin(medecin);
    }
    
    @PostMapping
    public Medecin createMedecin(@RequestBody Medecin medecin) {
        return medecinService.saveMedecin(medecin);
    }
   
    
    @GetMapping("/all")
    public List<Medecin> getAllMedecins() {
        return medecinService.getAllMedecins();
    }
    
    @GetMapping("/{id}")
    public Medecin getMedecinById(@PathVariable Long id) {
        return medecinService.getMedecinById(id);
    }
    
    @GetMapping("/specialite/{specialite}")
    public List<Medecin> getMedecinsBySpecialite(@PathVariable String specialite) {
        return medecinService.getMedecinsBySpecialite(specialite);
    }
    
    @DeleteMapping("/delete/{id}")
    public void deleteMedecin(@PathVariable Long id) {
        medecinService.deleteMedecin(id);
    }
}