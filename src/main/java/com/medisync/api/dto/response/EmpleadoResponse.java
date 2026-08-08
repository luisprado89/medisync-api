package com.medisync.api.dto.response;

import lombok.*;

import java.util.List;

/**
 * DTO utilizado para representar la información de un empleado
 * en las respuestas de la API.
 *
 * Contiene los datos básicos del empleado, su rol y las especialidades
 * médicas asociadas, evitando exponer directamente la entidad JPA.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EmpleadoResponse {

    /**
     * Identificador único del empleado.
     */
    private Long id;

    /**
     * Nombre de la persona asociada al empleado.
     */
    private String nombre;

    /**
     * Correo electrónico de la persona asociada al empleado.
     */
    private String email;

    /**
     * Rol asignado al empleado dentro del sistema.
     */
    private String rol;

    /**
     * Lista de especialidades médicas asociadas al empleado.
     */
    private List<EspecialidadResponse> especialidades;
}