package com.medisync.api.dto.request;

import com.medisync.api.enums.EstadoAtencion;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * DTO utilizado para transportar la información necesaria para actualizar
 * una atención médica existente.
 *
 * Todos los campos son opcionales, permitiendo modificar únicamente los
 * datos que se deseen actualizar. Las validaciones se aplican únicamente
 * sobre los campos proporcionados.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AtencionUpdateRequest {
    /**
     * Nueva fecha y hora de la atención médica.
     */
    private LocalDateTime fecha;
    /**
     * Nuevo motivo asociado a la atención médica.
     */
    @Size(max = 500, message = "El motivo no puede exceder los 500 caracteres")
    private String motivo;
    /**
     * Nuevo importe de la atención médica.
     */
    @Positive(message = "El importe debe ser positivo")
    private BigDecimal importe;
    /**
     * Nuevo estado de la atención médica.
     */
    private EstadoAtencion estado;
}