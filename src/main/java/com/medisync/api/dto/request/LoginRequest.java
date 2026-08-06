package com.medisync.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


/**
 * DTO utilizado para transportar las credenciales de autenticación
 * enviadas por el cliente durante el proceso de inicio de sesión.
 *
 * Contiene el nombre de usuario y la contraseña necesarios para
 * autenticar al usuario y generar un token JWT.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class LoginRequest {
    /**
     * Nombre de usuario utilizado para iniciar sesión.
     */
    @NotBlank(message = "El username es obligatorio")
    private String username;
    /**
     * Contraseña asociada al usuario.
     */
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}