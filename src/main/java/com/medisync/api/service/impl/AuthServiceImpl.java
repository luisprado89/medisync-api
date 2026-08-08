package com.medisync.api.service.impl;

import com.medisync.api.dto.request.LoginRequest;
import com.medisync.api.dto.request.RegistroRequest;
import com.medisync.api.dto.response.AuthResponse;
import com.medisync.api.entity.Empleado;
import com.medisync.api.entity.Paciente;
import com.medisync.api.entity.Persona;
import com.medisync.api.entity.Usuario;
import com.medisync.api.exception.BusinessRuleException;
import com.medisync.api.exception.ResourceNotFoundException;
import com.medisync.api.repository.EmpleadoRepository;
import com.medisync.api.repository.PacienteRepository;
import com.medisync.api.repository.PersonaRepository;
import com.medisync.api.repository.UsuarioRepository;
import com.medisync.api.security.JwtService;
import com.medisync.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.medisync.api.enums.RolUsuario.ADMIN;
import static com.medisync.api.enums.RolUsuario.MEDICO;

/**
 * Implementación de la lógica de autenticación y registro de usuarios.
 *
 * Gestiona el inicio de sesión mediante credenciales almacenadas de forma
 * segura y el registro de nuevos usuarios, incluyendo la creación del
 * perfil asociado según el rol seleccionado.
 *
 * Utiliza PasswordEncoder para proteger las contraseñas y JwtService
 * para generar los tokens JWT utilizados en las peticiones autenticadas.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final PacienteRepository pacienteRepository;
    private final EmpleadoRepository empleadoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Autentica un usuario mediante su nombre de usuario y contraseña.
     *
     * Comprueba que el usuario exista y esté activo, valida la contraseña
     * mediante PasswordEncoder y, si las credenciales son correctas,
     * genera un token JWT con el rol del usuario.
     *
     * @param loginRequest credenciales de acceso del usuario.
     * @return respuesta con el token JWT, username y rol.
     * @throws BusinessRuleException si las credenciales no son válidas.
     */
    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest loginRequest) {
        Usuario usuario = usuarioRepository.findByUsernameAndActivoTrue(loginRequest.getUsername())
                .orElseThrow(() -> new BusinessRuleException("Credenciales inválidas"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())) {
            throw new BusinessRuleException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(usuario.getUsername(), usuario.getRol().name());

        return AuthResponse.builder()
                .token(token)
                .username(usuario.getUsername())
                .rol(usuario.getRol().name())
                .build();
    }

    @Override
    @Transactional
    public AuthResponse registrar(RegistroRequest registroRequest) {
        // 1. Buscar la persona
        Persona persona = personaRepository.findById(registroRequest.getPersonaId())
                .filter(Persona::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada con id: " + registroRequest.getPersonaId()));

        // 2. Verificar que esa persona no tenga ya un usuario
        usuarioRepository.findByUsernameAndActivoTrue(registroRequest.getUsername())
                .ifPresent(u -> { throw new BusinessRuleException("El username ya está en uso"); });

        // 3. Crear el Usuario
        Usuario nuevoUsuario = Usuario.builder()
                .username(registroRequest.getUsername())
                .password(passwordEncoder.encode(registroRequest.getPassword()))
                .rol(registroRequest.getRol())
                .persona(persona)
                .activo(true)
                .build();
        usuarioRepository.save(nuevoUsuario);

        // 4. Según el rol, crear el registro en Paciente o Empleado
        if (registroRequest.getRol().name().equals("PACIENTE")) {
            // Validar si ya es paciente
            if (pacienteRepository.findByPersonaIdAndActivoTrue(persona.getId()).isPresent()) {
                throw new BusinessRuleException("La persona ya está registrada como paciente");
            }
            Paciente nuevoPaciente = Paciente.builder()
                    .persona(persona)
                    .activo(true)
                    .build();
            pacienteRepository.save(nuevoPaciente);
        } else {
            // Si es MEDICO o ADMIN, se registra como Empleado
            if (empleadoRepository.findByPersonaIdAndActivoTrue(persona.getId()).isPresent()) {
                throw new BusinessRuleException("La persona ya está registrada como empleado");
            }
            Empleado nuevoEmpleado = Empleado.builder()
                    .persona(persona)
                    .rol(registroRequest.getRol().name().equals("MEDICO") ? MEDICO : ADMIN)
                    .activo(true)
                    .build();
            empleadoRepository.save(nuevoEmpleado);
        }

        // 5. Generar token y devolver respuesta
        String token = jwtService.generateToken(nuevoUsuario.getUsername(), nuevoUsuario.getRol().name());

        return AuthResponse.builder()
                .token(token)
                .username(nuevoUsuario.getUsername())
                .rol(nuevoUsuario.getRol().name())
                .build();
    }
}