package com.khaoula.clinique.service;

import com.khaoula.clinique.entities.Patient;
import com.khaoula.clinique.entities.Role;
import com.khaoula.clinique.entities.User;
import com.khaoula.clinique.repository.PatientRepository;
import com.khaoula.clinique.repository.RoleRepository;
import com.khaoula.clinique.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public Patient savePatient(Patient patient) {
        // Récupérer ou créer l'utilisateur
        User user = patient.getUser();
        
        if (user == null) {
            throw new RuntimeException("User est null !");
        }
        
        // Vérifier si l'email existe déjà
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Email déjà utilisé !");
        }
        
        // Encoder le mot de passe
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Attribuer le rôle PATIENT
        Role patientRole = roleRepository.findByRole("PATIENT");
        if (patientRole == null) {
            patientRole = new Role();
            patientRole.setRole("PATIENT");
            roleRepository.save(patientRole);
        }
        user.setRoles(Arrays.asList(patientRole));
        user.setEnabled(true);
        
        // Sauvegarder l'utilisateur d'abord
        userRepository.save(user);
        
        // Sauvegarder le patient
        return patientRepository.save(patient);
    }
    
    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElse(null);
    }
    
    @Override
    public Patient getPatientByUsername(String username) {
        return patientRepository.findByUserUsername(username);
    }
    
    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
    
    @Override
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}