package com.medisync.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "persona")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    // RELACIÓN INVERSA AÑADIDA
    @OneToOne(mappedBy = "persona", fetch = FetchType.LAZY)
    private Usuario usuario;

    @Column(name = "activo", nullable = false)
    @Builder.Default// Booleano por defecto true. Recordamos que @Builder.Default es VITAL aquí.
    private Boolean activo = true;
}