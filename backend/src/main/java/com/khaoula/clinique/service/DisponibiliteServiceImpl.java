package com.khaoula.clinique.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khaoula.clinique.entities.Disponibilite;
import com.khaoula.clinique.repos.DisponibiliteRepository;

@Service
public class DisponibiliteServiceImpl implements DisponibiliteService {

    @Autowired
    private DisponibiliteRepository disponibiliteRepository;

    @Override
    public Disponibilite saveDisponibilite(Disponibilite disponibilite) {
        return disponibiliteRepository.save(disponibilite);
    }

    @Override
    public Disponibilite updateDisponibilite(Disponibilite disponibilite) {
        return disponibiliteRepository.save(disponibilite);
    }

    @Override
    public void deleteDisponibilite(Disponibilite disponibilite) {
        disponibiliteRepository.delete(disponibilite);
    }

    @Override
    public void deleteDisponibiliteById(Long id) {
        disponibiliteRepository.deleteById(id);
    }

    @Override
    public Optional<Disponibilite> getDisponibilite(Long id) {
        return disponibiliteRepository.findById(id);
    }

    @Override
    public List<Disponibilite> getAllDisponibilites() {
        return disponibiliteRepository.findAll();
    }

    @Override
    public List<Disponibilite> findByMedecinId(Long medecinId) {
        return disponibiliteRepository.findByMedecinId(medecinId);
    }
}