package com.itbs.clinique.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        System.out.println("=== DEBUT INITIALISATION ===");

        try {
            // 1. Créer les rôles s'ils n'existent pas
            Long rolePatientCount = (Long) entityManager
                    .createQuery("SELECT COUNT(r) FROM Role r WHERE r.role = 'PATIENT'")
                    .getSingleResult();
            if (rolePatientCount == 0) {
                entityManager.createNativeQuery(
                        "INSERT INTO role (role_id, role) VALUES (1, 'PATIENT')")
                        .executeUpdate();
                System.out.println("✅ Rôle PATIENT créé");
            }

            Long roleAdminCount = (Long) entityManager
                    .createQuery("SELECT COUNT(r) FROM Role r WHERE r.role = 'ADMIN'")
                    .getSingleResult();
            if (roleAdminCount == 0) {
                entityManager.createNativeQuery(
                        "INSERT INTO role (role_id, role) VALUES (2, 'ADMIN')")
                        .executeUpdate();
                System.out.println("✅ Rôle ADMIN créé");
            }

            Long roleMedecinCount = (Long) entityManager
                    .createQuery("SELECT COUNT(r) FROM Role r WHERE r.role = 'MEDECIN'")
                    .getSingleResult();
            if (roleMedecinCount == 0) {
                entityManager.createNativeQuery(
                        "INSERT INTO role (role_id, role) VALUES (3, 'MEDECIN')")
                        .executeUpdate();
                System.out.println("✅ Rôle MEDECIN créé");
            }

            // 2. Créer l'admin par défaut s'il n'existe pas
            // La colonne role_id référence directement la table role (ManyToOne)
            Long adminCount = (Long) entityManager
                    .createQuery("SELECT COUNT(u) FROM User u WHERE u.username = 'admin@clinique.com'")
                    .getSingleResult();
            if (adminCount == 0) {
                String encodedPassword = passwordEncoder.encode("admin123");
                entityManager.createNativeQuery(
                        "INSERT INTO user (enabled, nom, password, prenom, tel, username, role_id) " +
                        "VALUES (1, 'Admin', :password, 'System', '00000000', 'admin@clinique.com', 2)")
                        .setParameter("password", encodedPassword)
                        .executeUpdate();
                System.out.println("✅ Admin créé — email: admin@clinique.com / mdp: admin123");
            }

            System.out.println("🎉 Initialisation terminée !");

        } catch (Exception e) {
            System.out.println("❌ Erreur initialisation: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== FIN INITIALISATION ===");
    }
}
