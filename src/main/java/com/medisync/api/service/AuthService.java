package com.medisync.api.service;

import com.medisync.api.dto.request.LoginRequest;
import com.medisync.api.dto.response.AuthResponse;

/**
 * Define las operaciones relacionadas con el proceso de autenticación
 * de usuarios.
 *
 * Establece el contrato que deben implementar los servicios encargados
 * de validar las credenciales y generar la respuesta de autenticación.
 */
public interface AuthService {

    /**
     * Autentica a un usuario a partir de sus credenciales.
     *
     * Valida la información recibida y, si la autenticación es correcta,
     * devuelve un token JWT junto con la información básica del usuario.
     *
     * @param request credenciales utilizadas para iniciar sesión.
     * @return respuesta con el token JWT y los datos del usuario autenticado.
     */
    AuthResponse login(LoginRequest request);
}