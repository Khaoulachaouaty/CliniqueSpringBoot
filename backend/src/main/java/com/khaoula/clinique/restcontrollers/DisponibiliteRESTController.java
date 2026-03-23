package com.khaoula.clinique.restcontrollers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.khaoula.clinique.entities.Disponibilite;
import com.khaoula.clinique.service.DisponibiliteService;

@RestController
@RequestMapping("/api/disponibilites")
@CrossOrigin(origins = "*")
public class DisponibiliteRESTController {

    @Autowired
    private DisponibiliteService disponibiliteService;

    @GetMapping
    public List<Disponibilite> getAllDisponibilites() {
        return disponibiliteService.getAllDisponibilites();
    }

    @GetMapping("/{id}")
    public Disponibilite getDisponibiliteById(@PathVariable Long id) {
        return disponibiliteService.getDisponibilite(id).orElse(null);
    }

    @PostMapping
    public Disponibilite createDisponibilite(@RequestBody Disponibilite disponibilite) {
        return disponibiliteService.saveDisponibilite(disponibilite);
    }

    @PutMapping("/{id}")
    public Disponibilite updateDisponibilite(@PathVariable Long id, @RequestBody Disponibilite disponibilite) {
        disponibilite.setId(id);
        return disponibiliteService.updateDisponibilite(disponibilite);
    }

    @DeleteMapping("/{id}")
    public void deleteDisponibilite(@PathVariable Long id) {
        disponibiliteService.deleteDisponibiliteById(id);
    }

    @GetMapping("/medecin/{medecinId}")
    public List<Disponibilite> getDisponibilitesByMedecin(@PathVariable Long medecinId) {
        return disponibiliteService.findByMedecinId(medecinId);
    }
}