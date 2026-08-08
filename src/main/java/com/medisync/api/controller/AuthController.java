package com.medisync.api.controller;

import com.medisync.api.dto.request.LoginRequest;
import com.medisync.api.dto.response.AuthResponse;
import com.medisync.api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST encargado de gestionar la autenticación de los usuarios.
 *
 * Expone los endpoints necesarios para iniciar sesión y obtener un token
 * JWT que será utilizado posteriormente para acceder a los recursos
 * protegidos de la aplicación.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoint para el inicio de sesión y generación de JWT")
public class AuthController {
    /**
     * Servicio encargado de gestionar el proceso de autenticación.
     */
    private final AuthService authService;
    /**
     * Auténtica a un usuario mediante sus credenciales.
     *
     * Válida los datos recibidos y delega el proceso de autenticación
     * en la capa de servicio. Si las credenciales son válidas, devuelve
     * un token JWT junto con la información básica del usuario autenticado.
     *
     * @param request datos de acceso del usuario.
     * @return respuesta que contiene el token JWT y los datos del usuario.
     */
    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y devuelve un token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
            @ApiResponse(responseCode = "400", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}