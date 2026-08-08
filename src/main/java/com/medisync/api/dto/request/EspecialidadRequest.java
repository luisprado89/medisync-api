package com.medisync.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO utilizado para recibir los datos necesarios para crear o actualizar
 * una especialidad médica.
 *
 * Incluye validaciones para garantizar que el nombre sea obligatorio
 * y no supere la longitud máxima permitida.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EspecialidadRequest {

    /**
     * Nombre de la especialidad médica.
     *
     * Debe ser obligatorio y no puede superar los 100 caracteres.
     */
    @NotBlank(message = "El nombre de la especialidad es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;
}
