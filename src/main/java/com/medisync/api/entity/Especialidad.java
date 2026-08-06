package com.medisync.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "especialidad")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Especialidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @ManyToMany(mappedBy = "especialidades")
    @Builder.Default
    private List<Empleado> medicos = new ArrayList<>();

    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;
}