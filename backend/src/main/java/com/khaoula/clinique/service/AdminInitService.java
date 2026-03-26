package com.khaoula.clinique.service;

/*import com.khaoula.clinique.entities.Role;
import com.khaoula.clinique.entities.User;
import com.khaoula.clinique.repository.RoleRepository;
import com.khaoula.clinique.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class AdminInitService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @PostConstruct
    public void init() {
        try {
            // Étape 1 : Créer tous les rôles d'abord (sans lien avec user)
            createRoleIfNotExists("ADMIN");
            createRoleIfNotExists("MEDECIN");
            createRoleIfNotExists("PATIENT");
            
            // Étape 2 : Vérifier si admin existe
            if (userRepository.findByUsername("admin@clinique.com") != null) {
                System.out.println("ℹ️ Admin existe déjà");
                return;
            }
            
            // Étape 3 : Récupérer le rôle ADMIN fraîchement depuis la base
            Role adminRole = roleRepository.findByRole("ADMIN");
            
            if (adminRole == null) {
                System.err.println("❌ Rôle ADMIN non trouvé après création !");
                return;
            }
            
            // Étape 4 : Créer l'admin SANS utiliser la liste de rôles directement
            User admin = new User();
            admin.setUsername("admin@clinique.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setNom("Admin");
            admin.setPrenom("System");
            admin.setTel("00000000");
            admin.setEnabled(true);
            
            // Sauvegarder l'admin d'abord SANS rôles
            admin = userRepository.save(admin);
            System.out.println("✅ User admin sauvegardé (ID: " + admin.getUserId() + ")");
            
            // Étape 5 : Ajouter le rôle APRÈS la première sauvegarde
            admin.setRoles(new ArrayList<>(Arrays.asList(adminRole)));
            userRepository.save(admin);
            
            System.out.println("✅ Admin créé avec succès: admin@clinique.com / admin123");
            
        } catch (Exception e) {
            System.err.println("❌ Erreur création admin: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void createRoleIfNotExists(String roleName) {
        Role existingRole = roleRepository.findByRole(roleName);
        if (existingRole == null) {
            Role newRole = new Role();
            newRole.setRole(roleName);
            roleRepository.save(newRole);
            System.out.println("✅ Rôle " + roleName + " créé");
        } else {
            System.out.println("ℹ️ Rôle " + roleName + " existe déjà");
        }
    }
}*/