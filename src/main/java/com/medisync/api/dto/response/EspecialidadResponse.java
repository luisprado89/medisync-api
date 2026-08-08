package com.medisync.api.dto.response;

import lombok.*;

/**
 * DTO utilizado para representar la información de una especialidad médica
 * en las respuestas de la API.
 *
 * Contiene el identificador y el nombre de la especialidad.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EspecialidadResponse {

    /**
     * Identificador único de la especialidad.
     */
    private Long id;

    /**
     * Nombre de la especialidad médica.
     */
    private String nombre;
}
