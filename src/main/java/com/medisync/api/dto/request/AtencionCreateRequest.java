package com.medisync.api.dto.request;

import com.medisync.api.enums.EstadoAtencion;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO utilizado para transportar la información necesaria para registrar
 * una nueva atención médica.
 *
 * Incluye las validaciones requeridas para garantizar la integridad de los
 * datos recibidos antes de ser procesados por la capa de servicio.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AtencionCreateRequest {

    /**
     * Fecha y hora programada para la atención médica.
     */
    @NotNull(message = "La fecha es obligatoria")
    private LocalDateTime fecha;
    /**
     * Motivo por el que se solicita la atención médica.
     */
    @NotBlank(message = "El motivo es obligatorio")
    @Size(max = 500, message = "El motivo no puede exceder los 500 caracteres")
    private String motivo;
    /**
     * Importe asociado a la atención médica.
     */
    @NotNull(message = "El importe es obligatorio")
    @Positive(message = "El importe debe ser positivo")
    @Digits(integer = 10, fraction = 2, message = "El importe debe tener como máximo 2 decimales")
    private BigDecimal importe;

    /**
     * Identificador del paciente al que pertenece la atención.
     */
    @NotNull(message = "El ID del paciente es obligatorio")
    @Positive(message = "El ID del paciente debe ser positivo")
    private Long pacienteId;
    /**
     * Identificador del empleado responsable de la atención.
     */
    @NotNull(message = "El ID del empleado es obligatorio")
    @Positive(message = "El ID del empleado debe ser positivo")
    private Long empleadoId;
    /**
     * Estado inicial de la atención médica.
     */
    @NotNull(message = "El estado es obligatorio")
    private EstadoAtencion estado;
}