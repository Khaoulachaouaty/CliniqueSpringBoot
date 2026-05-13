package com.itbs.clinique.config;

import com.itbs.clinique.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity   // Active @PreAuthorize sur les controllers
public class SimpleSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SimpleSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Spring Boot auto-configure DaoAuthenticationProvider via le bean UserDetailsServiceImpl
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // ── Endpoints publics ──────────────────────────────────────
                .requestMatchers(
                    "/api/auth/**",
                    "/api/public/**"
                ).permitAll()

                // ── Swagger / Actuator ─────────────────────────────────────
                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/api-docs/**",
                    "/api-docs.yaml",
                    "/actuator/**"
                ).permitAll()

                // ── Admin uniquement ───────────────────────────────────────
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // ── Médecin + Admin ────────────────────────────────────────
                .requestMatchers(
                    "/api/consultations/**",
                    "/api/dossiers-medicaux/**"
                ).hasAnyRole("MEDECIN", "ADMIN")

                // ── Tout utilisateur authentifié ───────────────────────────
                .requestMatchers(
                    "/api/rendezvous/**",
                    "/api/notifications/**"
                ).authenticated()

                // ── Tout le reste : authentifié ────────────────────────────
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
