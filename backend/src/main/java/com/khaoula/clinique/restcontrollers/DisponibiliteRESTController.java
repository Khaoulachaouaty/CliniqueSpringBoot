package com.khaoula.clinique.restcontrollers;

import com.khaoula.clinique.entities.Disponibilite;
import com.khaoula.clinique.service.DisponibiliteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disponibilites")
@CrossOrigin(origins = "*")
public class DisponibiliteRESTController {
    
    @Autowired
    private DisponibiliteService disponibiliteService;
    
    @PostMapping("/save")
    public Disponibilite saveDisponibilite(@RequestBody Disponibilite disponibilite) {
        return disponibiliteService.saveDisponibilite(disponibilite);
    }
    
    @GetMapping("/all")
    public List<Disponibilite> getAllDisponibilites() {
        return disponibiliteService.getAllDisponibilites();
    }
    
    @GetMapping("/{id}")
    public Disponibilite getDisponibiliteById(@PathVariable Long id) {
        return disponibiliteService.getDisponibiliteById(id);
    }
    
    @GetMapping("/medecin/{medecinId}")
    public List<Disponibilite> getDisponibilitesByMedecin(@PathVariable Long medecinId) {
        return disponibiliteService.getDisponibilitesByMedecin(medecinId);
    }
    
    @DeleteMapping("/delete/{id}")
    public void deleteDisponibilite(@PathVariable Long id) {
        disponibiliteService.deleteDisponibilite(id);
    }
}