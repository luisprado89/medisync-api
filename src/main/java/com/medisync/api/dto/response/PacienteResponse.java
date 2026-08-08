package com.medisync.api.dto.response;

import lombok.*;

/**
 * DTO utilizado para representar la información de un paciente
 * en las respuestas de la API.
 *
 * Contiene los datos básicos del paciente y evita exponer
 * directamente la entidad {@code Paciente} de la capa de persistencia.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PacienteResponse {

    /**
     * Identificador único del paciente.
     */
    private Long id;

    /**
     * Nombre de la persona asociada al paciente.
     */
    private String nombre;

    /**
     * Correo electrónico de la persona asociada al paciente.
     */
    private String email;
}