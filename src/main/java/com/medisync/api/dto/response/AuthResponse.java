package com.medisync.api.dto.response;

import lombok.*;

/**
 * DTO utilizado para devolver la información generada tras una
 * autenticación satisfactoria.
 *
 * Contiene el token JWT que deberá incluirse en las siguientes
 * peticiones autenticadas, junto con información básica del usuario.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AuthResponse {
    /**
     * Token JWT generado tras la autenticación del usuario.
     */
    private String token;
    /**
     * Nombre del usuario autenticado.
     */
    private String username;
    /**
     * Rol asignado al usuario autenticado.
     */
    private String rol;
}