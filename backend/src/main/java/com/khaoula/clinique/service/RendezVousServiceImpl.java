package com.khaoula.clinique.service;

import com.khaoula.clinique.entities.RendezVous;
import com.khaoula.clinique.repository.RendezVousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RendezVousServiceImpl implements RendezVousService {
    
    @Autowired
    private RendezVousRepository rendezVousRepository;
    
    @Override
    public RendezVous saveRendezVous(RendezVous rendezVous) {
        rendezVous.setStatut("EN_ATTENTE");
        return rendezVousRepository.save(rendezVous);
    }
    
    @Override
    public RendezVous getRendezVousById(Long id) {
        return rendezVousRepository.findById(id).orElse(null);
    }
    
    @Override
    public List<RendezVous> getRendezVousByPatient(Long patientId) {
        return rendezVousRepository.findByPatientId(patientId);
    }
    
    @Override
    public List<RendezVous> getRendezVousByMedecin(Long medecinId) {
        return rendezVousRepository.findByMedecinId(medecinId);
    }
    
    @Override
    public List<RendezVous> getRendezVousByMedecinAndDate(Long medecinId, Date date) {
        return rendezVousRepository.findByMedecinIdAndDate(medecinId, date);
    }
    
    @Override
    public List<RendezVous> getAllRendezVous() {
        return rendezVousRepository.findAll();
    }
    
    @Override
    public RendezVous accepterRendezVous(Long id) {
        RendezVous rdv = getRendezVousById(id);
        if (rdv != null) {
            rdv.setStatut("ACCEPTE");
            return rendezVousRepository.save(rdv);
        }
        return null;
    }
    
    @Override
    public RendezVous refuserRendezVous(Long id) {
        RendezVous rdv = getRendezVousById(id);
        if (rdv != null) {
            rdv.setStatut("REFUSE");
            return rendezVousRepository.save(rdv);
        }
        return null;
    }
    
    @Override
    public void deleteRendezVous(Long id) {
        rendezVousRepository.deleteById(id);
    }
    
    @Override
    public boolean verifierDisponibilite(Long medecinId, Date date, String heure) {
        List<RendezVous> rdvs = rendezVousRepository.findByMedecinIdAndDate(medecinId, date);
        for (RendezVous rdv : rdvs) {
            if (rdv.getHeure().equals(heure) && 
                (rdv.getStatut().equals("ACCEPTE") || rdv.getStatut().equals("EN_ATTENTE"))) {
                return false;
            }
        }
        return true;
    }
}