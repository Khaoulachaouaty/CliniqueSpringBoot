package com.khaoula.clinique.services;

import com.khaoula.clinique.dto.*;
import com.khaoula.clinique.entities.*;
import com.khaoula.clinique.repositories.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PatientRepository patientRepository;
    private final MedecinRepository medecinRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                          PatientRepository patientRepository, MedecinRepository medecinRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.patientRepository = patientRepository;
        this.medecinRepository = medecinRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public MessageResponse registerPatient(RegisterPatientRequest request) {
        // Vérifier si email existe déjà
        if (userRepository.existsByUsername(request.getEmail())) {
            return new MessageResponse("Cet email est déjà utilisé", false);
        }
        
        // Créer ou récupérer le rôle PATIENT
        Role patientRole = roleRepository.findByRole("PATIENT")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRole("PATIENT");
                    return roleRepository.save(newRole);
                });
        
        // Créer l'utilisateur
        User user = new User();
        user.setUsername(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setTel(request.getTel());
        user.setEnabled(true);
        user.setRoles(Collections.singletonList(patientRole));
        
        User savedUser = userRepository.save(user);
        
        // Créer le patient
        Patient patient = new Patient();
        patient.setUser(savedUser);
        patient.setDateNaissance(request.getDateNaissance());
        patient.setDossierMedical(request.getDossierMedical());
        patientRepository.save(patient);
        
        return new MessageResponse("Patient inscrit avec succès", true);
    }

    @Override
    public MessageResponse createMedecin(CreateMedecinRequest request) {
        // Vérifier si email existe déjà
        if (userRepository.existsByUsername(request.getEmail())) {
            return new MessageResponse("Cet email est déjà utilisé", false);
        }
        
        // Créer ou récupérer le rôle MEDECIN
        Role medecinRole = roleRepository.findByRole("MEDECIN")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRole("MEDECIN");
                    return roleRepository.save(newRole);
                });
        
        // Créer l'utilisateur
        User user = new User();
        user.setUsername(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setTel(request.getTel());
        user.setEnabled(true);
        user.setRoles(Collections.singletonList(medecinRole));
        
        User savedUser = userRepository.save(user);
        
        // Créer le médecin
        Medecin medecin = new Medecin();
        medecin.setUser(savedUser);
        medecin.setSpecialite(request.getSpecialite());
        medecinRepository.save(medecin);
        
        return new MessageResponse("Médecin créé avec succès", true);
    }

    @Override
    public MessageResponse login(LoginRequest request) {
        // Chercher l'utilisateur
        User user = userRepository.findByUsername(request.getEmail())
                .orElse(null);
        
        if (user == null) {
            return new MessageResponse("Email ou mot de passe incorrect", false);
        }
        
        // Vérifier le mot de passe
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new MessageResponse("Email ou mot de passe incorrect", false);
        }
        
        // Vérifier si compte actif
        if (user.getEnabled() == null || !user.getEnabled()) {
            return new MessageResponse("Compte désactivé", false);
        }
        
        return new MessageResponse("Connexion réussie", true);
    }
}