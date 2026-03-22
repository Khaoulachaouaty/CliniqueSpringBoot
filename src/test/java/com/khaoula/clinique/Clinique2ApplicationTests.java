package com.khaoula.clinique;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.khaoula.clinique.entities.Medecin;
import com.khaoula.clinique.repos.MedecinRepository;

@SpringBootTest
class Clinique2ApplicationTests {

    @Autowired
    private MedecinRepository medecinRepository;

    @Test
    void testAjouterMedecin() {
        Medecin m = new Medecin();
        m.setNom("Dr Ahmed");
        m.setEmail("ahmed@test.com");
        m.setPassword("pass123");
        m.setSpecialite("Généraliste");
        
        Medecin saved = medecinRepository.save(m);
        
        System.out.println("✅ Médecin ajouté - ID: " + saved.getId());
    }
}