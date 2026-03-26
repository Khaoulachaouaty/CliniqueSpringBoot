package com.khaoula.clinique;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.khaoula.clinique.entities.Medecin;
import com.khaoula.clinique.repository.MedecinRepository;

@SpringBootTest
class Clinique2ApplicationTests {

    @Autowired
    private MedecinRepository medecinRepository;

}