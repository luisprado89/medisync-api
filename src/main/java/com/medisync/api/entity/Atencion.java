package com.medisync.api.entity;

import com.medisync.api.enums.EstadoAtencion;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "atencion")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Atencion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "motivo", nullable = false, length = 500)
    private String motivo;

    // Uso de BigDecimal para precisión monetaria (lo que paga el paciente)
    @Column(name = "importe", precision = 10, scale = 2)
    private BigDecimal importe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAtencion estado;

    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;
}