package com.khaoula.clinique.service;

import com.khaoula.clinique.entities.Medecin;
import com.khaoula.clinique.entities.Role;
import com.khaoula.clinique.entities.User;
import com.khaoula.clinique.repository.MedecinRepository;
import com.khaoula.clinique.repository.RoleRepository;
import com.khaoula.clinique.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MedecinServiceImpl implements MedecinService {
    
    @Autowired
    private MedecinRepository medecinRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public Medecin saveMedecin(Medecin medecin) {
        User user = medecin.getUser();
        
        if (user == null) {
            throw new RuntimeException("User est null !");
        }
        
        // Vérifier si email existe
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Email déjà utilisé !");
        }
        
        // Encoder mot de passe
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Attribuer rôle MEDECIN
        Role medecinRole = roleRepository.findByRole("MEDECIN");
        if (medecinRole == null) {
            medecinRole = new Role();
            medecinRole.setRole("MEDECIN");
            roleRepository.save(medecinRole);
        }
        user.setRoles(Arrays.asList(medecinRole));
        user.setEnabled(true);
        
        // Sauvegarder user d'abord
        userRepository.save(user);
        
        // Sauvegarder médecin
        return medecinRepository.save(medecin);
    }
    
    @Override
    public Medecin getMedecinById(Long id) {
        return medecinRepository.findById(id).orElse(null);
    }
    
    @Override
    public List<Medecin> getAllMedecins() {
        return medecinRepository.findAll();
    }
    
    @Override
    public void deleteMedecin(Long id) {
        medecinRepository.deleteById(id);
    }

	@Override
	public Medecin getMedecinByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Medecin> getMedecinsBySpecialite(String specialite) {
		// TODO Auto-generated method stub
		return null;
	}
}