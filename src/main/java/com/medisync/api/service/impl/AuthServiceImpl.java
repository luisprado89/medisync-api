package com.medisync.api.service.impl;

import com.medisync.api.dto.request.LoginRequest;
import com.medisync.api.dto.response.AuthResponse;
import com.medisync.api.entity.Usuario;
import com.medisync.api.exception.BusinessRuleException;
import com.medisync.api.repository.UsuarioRepository;
import com.medisync.api.security.JwtService;
import com.medisync.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio de autenticación.
 *
 * Gestiona el proceso de inicio de sesión validando las credenciales
 * del usuario y generando un token JWT cuando la autenticación es
 * satisfactoria.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    /**
     * Repositorio utilizado para recuperar los usuarios almacenados
     * en la base de datos.
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * Componente encargado de verificar las contraseñas cifradas.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Servicio responsable de generar los tokens JWT.
     */
    private final JwtService jwtService;

    /**
     * Autentica a un usuario a partir de las credenciales recibidas.
     *
     * Comprueba que el usuario exista y se encuentre activo, verifica la
     * contraseña proporcionada y, si la autenticación es correcta, genera
     * un token JWT que será utilizado en las siguientes peticiones.
     *
     * @param request credenciales proporcionadas por el usuario.
     * @return respuesta con el token JWT y la información básica del usuario.
     * @throws BusinessRuleException si las credenciales son incorrectas.
     */
    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByUsernameAndActivoTrue(request.getUsername())
                .orElseThrow(() -> new BusinessRuleException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new BusinessRuleException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(usuario.getUsername(), usuario.getRol().name());

        return AuthResponse.builder()
                .token(token)
                .username(usuario.getUsername())
                .rol(usuario.getRol().name())
                .build();
    }
}