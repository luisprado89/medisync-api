package com.medisync.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO utilizado para recibir los datos necesarios para crear o actualizar
 * una persona.
 *
 * Incluye validaciones para garantizar que el nombre y el correo electrónico
 * sean obligatorios y que el correo tenga un formato válido.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PersonaRequest {

    /**
     * Nombre completo de la persona.
     */
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    /**
     * Dirección de correo electrónico de la persona.
     */
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;
}
