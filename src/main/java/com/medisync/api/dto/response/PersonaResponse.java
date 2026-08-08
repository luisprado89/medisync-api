package com.medisync.api.dto.response;

import lombok.*;

/**
 * DTO utilizado para representar la información de una persona
 * en las respuestas de la API.
 *
 * Contiene el identificador y los datos básicos de la persona.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PersonaResponse {

    /**
     * Identificador único de la persona.
     */
    private Long id;

    /**
     * Nombre completo de la persona.
     */
    private String nombre;

    /**
     * Dirección de correo electrónico de la persona.
     */
    private String email;
}
