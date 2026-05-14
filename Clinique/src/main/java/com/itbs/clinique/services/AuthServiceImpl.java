package com.itbs.clinique.services;

import com.itbs.clinique.dto.*;
import com.itbs.clinique.entities.*;
import com.itbs.clinique.repositories.*;
import com.itbs.clinique.security.JwtUtils;
import com.itbs.clinique.security.UserDetailsServiceImpl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PatientRepository patientRepository;
    private final MedecinRepository medecinRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PatientRepository patientRepository, MedecinRepository medecinRepository,
                           PasswordEncoder passwordEncoder, JwtUtils jwtUtils,
                           UserDetailsServiceImpl userDetailsService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.patientRepository = patientRepository;
        this.medecinRepository = medecinRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public AuthResponse registerPatient(RegisterPatientRequest request) {
        if (userRepository.existsByUsername(request.getEmail())) {
            return AuthResponse.builder()
                    .message("Cet email est déjà utilisé")
                    .success(false)
                    .build();
        }

        Role patientRole = roleRepository.findByRole("PATIENT")
                .orElseThrow(() -> new RuntimeException("Rôle PATIENT introuvable"));

        User user = new User();
        user.setUsername(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setTel(request.getTel());
        user.setEnabled(true);
        user.setRole(patientRole);

        User savedUser = userRepository.save(user);

        Patient patient = new Patient();
        patient.setUser(savedUser);
        patient.setDateNaissance(request.getDateNaissance());
        patient.setDossierMedical(request.getDossierMedical());
        patientRepository.save(patient);

        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getUsername());
        String token = jwtUtils.generateToken(userDetails);

        Long patientId = patientRepository.findByUserUserId(savedUser.getUserId())
                .map(Patient::getId).orElse(null);

        return AuthResponse.builder()
                .message("Patient inscrit avec succès")
                .success(true)
                .userId(savedUser.getUserId())
                .email(savedUser.getUsername())
                .nomComplet(savedUser.getNomComplet())
                .roles(Collections.singletonList("PATIENT"))
                .token(token)
                .patientId(patientId)
                .build();
    }

    @Override
    public AuthResponse createMedecin(CreateMedecinRequest request) {
        if (userRepository.existsByUsername(request.getEmail())) {
            return AuthResponse.builder()
                    .message("Cet email est déjà utilisé")
                    .success(false)
                    .build();
        }

        Role medecinRole = roleRepository.findByRole("MEDECIN")
                .orElseThrow(() -> new RuntimeException("Rôle MEDECIN introuvable"));

        User user = new User();
        user.setUsername(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setTel(request.getTel());
        user.setEnabled(true);
        user.setRole(medecinRole);

        User savedUser = userRepository.save(user);

        Medecin medecin = new Medecin();
        medecin.setUser(savedUser);
        medecin.setSpecialite(request.getSpecialite());
        medecinRepository.save(medecin);

        Long medecinId = medecinRepository.findByUserUserId(savedUser.getUserId())
                .map(Medecin::getId).orElse(null);

        return AuthResponse.builder()
                .message("Médecin créé avec succès")
                .success(true)
                .userId(savedUser.getUserId())
                .email(savedUser.getUsername())
                .nomComplet(savedUser.getNomComplet())
                .roles(Collections.singletonList("MEDECIN"))
                .medecinId(medecinId)
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getEmail()).orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return AuthResponse.builder()
                    .message("Email ou mot de passe incorrect")
                    .success(false)
                    .build();
        }

        if (user.getEnabled() == null || !user.getEnabled()) {
            return AuthResponse.builder()
                    .message("Compte désactivé")
                    .success(false)
                    .build();
        }

        Long patientId = patientRepository.findByUserUserId(user.getUserId())
                .map(Patient::getId).orElse(null);
        Long medecinId = medecinRepository.findByUserUserId(user.getUserId())
                .map(Medecin::getId).orElse(null);

        String roleName = user.getRole().getRole();

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtUtils.generateToken(userDetails);

        System.out.println("✅ Login: " + user.getUsername() +
                " | Rôle: " + roleName +
                " | PatientId: " + patientId +
                " | MedecinId: " + medecinId);

        return AuthResponse.builder()
                .message("Connexion réussie")
                .success(true)
                .userId(user.getUserId())
                .email(user.getUsername())
                .nomComplet(user.getNomComplet())
                .roles(Collections.singletonList(roleName))
                .token(token)
                .patientId(patientId)
                .medecinId(medecinId)
                .build();
    }
}
