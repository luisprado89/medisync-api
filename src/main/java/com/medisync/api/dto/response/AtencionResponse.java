package com.medisync.api.dto.response;

import com.medisync.api.enums.EstadoAtencion;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO utilizado para devolver la información de una atención médica
 * al cliente.
 *
 * Además de los datos propios de la atención, incluye información
 * descriptiva del paciente y del empleado responsable para facilitar
 * su visualización sin necesidad de realizar consultas adicionales.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AtencionResponse {
    /**
     * Identificador único de la atención médica.
     */
    private Long id;
    /**
     * Fecha y hora de la atención médica.
     */
    private LocalDateTime fecha;
    /**
     * Motivo de la atención médica.
     */
    private String motivo;
    /**
     * Importe asociado a la atención médica.
     */
    private BigDecimal importe;
    /**
     * Estado actual de la atención médica.
     */
    private EstadoAtencion estado;
    // Enriquecemos la respuesta con los nombres, no solo los IDs
    /**
     * Nombre completo del paciente asociado a la atención.
     */
    private String pacienteNombre;
    /**
     * Nombre completo del empleado responsable de la atención.
     */
    private String empleadoNombre;
}