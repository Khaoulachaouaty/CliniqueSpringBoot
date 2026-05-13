package com.itbs.clinique.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
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
                                
                                ## Fonctionnalités
                                - **Authentification** : inscription patient, connexion
                                - **Administration** : gestion des médecins et patients
                                - **Rendez-vous** : prise, modification, annulation, créneaux disponibles
                                - **Consultations** : création, facturation, statistiques
                                - **Dossiers médicaux** : consultation et mise à jour
                                - **Notifications** : envoi, lecture, compteurs
                                - **Public** : liste des médecins par spécialité
                                
                                ## Base URL
                                `http://34.52.221.97/clinique`
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Khaoula Chaouaty")
                                .email("khaoulachaouatyy@gmail.com"))
                        .license(new License()
                                .name("Privé")))
                .servers(List.of(
                        new Server()
                                .url("http://34.52.221.97/clinique")
                                .description("Production — Google Cloud (GKE)"),
                        new Server()
                                .url("http://localhost:8082/clinique")
                                .description("Développement local")))
                .tags(List.of(
                        new Tag().name("Authentification")
                                .description("Inscription et connexion des utilisateurs"),
                        new Tag().name("Administration")
                                .description("Gestion des médecins et patients (accès admin)"),
                        new Tag().name("Rendez-vous")
                                .description("Prise, modification et annulation de rendez-vous"),
                        new Tag().name("Consultations")
                                .description("Gestion des consultations médicales et facturation"),
                        new Tag().name("Dossiers Médicaux")
                                .description("Consultation et mise à jour des dossiers patients"),
                        new Tag().name("Notifications")
                                .description("Gestion des notifications pour patients et médecins"),
                        new Tag().name("Public")
                                .description("Endpoints publics sans authentification")));
    }
}
