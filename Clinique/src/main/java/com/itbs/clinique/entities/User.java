package com.itbs.clinique.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"role", "patient", "medecin"})
@EqualsAndHashCode(exclude = {"role", "patient", "medecin"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String username; // email

    private String password;

    private String nom;

    private String prenom;

    private String tel;

    private Boolean enabled;

    // Un utilisateur a exactement un rôle (ADMIN, MEDECIN ou PATIENT)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne(mappedBy = "user")
    private Patient patient;

    @OneToOne(mappedBy = "user")
    private Medecin medecin;

    public String getNomComplet() {
        return (prenom != null ? prenom : "") + " " + (nom != null ? nom : "");
    }
}
