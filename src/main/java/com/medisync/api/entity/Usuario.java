package com.medisync.api.entity;

import com.medisync.api.enums.RolUsuario;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username; // NOT NULL. UNIQUE, VARCHAR(100) en MySQL.

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING) // Guarda el texto literalmente en MySQL -> PACIENTE
    @Column(nullable = false, length = 50)
    private RolUsuario rol; // PACIENTE, MEDICO, ADMIN

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona persona;

    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;
}