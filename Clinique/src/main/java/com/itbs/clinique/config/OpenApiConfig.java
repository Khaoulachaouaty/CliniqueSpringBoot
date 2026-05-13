package com.itbs.clinique.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.servlet.context-path:/clinique}")
    private String contextPath;

    @Bean
    public OpenAPI cliniqueOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Clinique API")
                        .description("""
                                API REST de gestion d'une clinique médicale.
                                
                                ## Authentification
                                Cette API utilise **JWT Bearer Token**.
                                1. Appelez `POST /api/auth/login` avec vos identifiants
                                2. Copiez le champ `token` de la réponse
                                3. Cliquez sur **Authorize** (🔒) en haut de cette page
                                4. Entrez : `Bearer <votre_token>`
                                
                                ## Rôles et accès
                                | Rôle | Accès |
                                |------|-------|
                                | `ADMIN` | Tout + gestion médecins/patients |
                                | `MEDECIN` | Consultations, dossiers, rendez-vous |
                                | `PATIENT` | Ses rendez-vous, son dossier, ses notifications |
                                
                                ## Compte admin par défaut
                                - Email : `admin@clinique.com`
                                - Mot de passe : `admin123`
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Khaoula Chaouaty")
                                .email("khaoulachaouatyy@gmail.com"))
                        .license(new License().name("Privé")))
                .servers(List.of(
                        new Server()
                                .url("http://104.155.112.106:8082/clinique")
                                .description("Production — Google Cloud (GKE)"),
                        new Server()
                                .url("http://localhost:8082/clinique")
                                .description("Développement local")))
                // Schéma de sécurité JWT Bearer
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Entrez le token JWT obtenu via POST /api/auth/login")))
                .tags(List.of(
                        new Tag().name("Authentification")
                                .description("Inscription et connexion — endpoints publics"),
                        new Tag().name("Administration")
                                .description("Gestion des médecins et patients — ADMIN uniquement"),
                        new Tag().name("Rendez-vous")
                                .description("Prise, modification et annulation — tous les rôles"),
                        new Tag().name("Consultations")
                                .description("Consultations médicales et facturation — MEDECIN / ADMIN"),
                        new Tag().name("Dossiers Médicaux")
                                .description("Consultation et mise à jour — MEDECIN / ADMIN / PATIENT"),
                        new Tag().name("Notifications")
                                .description("Notifications pour patients et médecins — authentifié"),
                        new Tag().name("Public")
                                .description("Endpoints publics sans authentification")));
    }
}
