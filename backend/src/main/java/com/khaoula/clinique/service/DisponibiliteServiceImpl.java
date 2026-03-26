package com.khaoula.clinique.service;

import com.khaoula.clinique.entities.Disponibilite;
import com.khaoula.clinique.repository.DisponibiliteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisponibiliteServiceImpl implements DisponibiliteService {
    
    @Autowired
    private DisponibiliteRepository disponibiliteRepository;
    
    @Override
    public Disponibilite saveDisponibilite(Disponibilite disponibilite) {
        return disponibiliteRepository.save(disponibilite);
    }
    
    @Override
    public Disponibilite getDisponibiliteById(Long id) {
        return disponibiliteRepository.findById(id).orElse(null);
    }
    
    @Override
    public List<Disponibilite> getDisponibilitesByMedecin(Long medecinId) {
        return disponibiliteRepository.findByMedecinId(medecinId);
    }
    
    @Override
    public List<Disponibilite> getAllDisponibilites() {
        return disponibiliteRepository.findAll();
    }
    
    @Override
    public void deleteDisponibilite(Long id) {
        disponibiliteRepository.deleteById(id);
    }
} 	