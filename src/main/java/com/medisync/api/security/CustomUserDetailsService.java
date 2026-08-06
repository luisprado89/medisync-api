package com.medisync.api.security;

import com.medisync.api.entity.Usuario;
import com.medisync.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Implementación personalizada de {@link UserDetailsService}.
 *
 * Se encarga de recuperar la información de un usuario desde la base de
 * datos durante el proceso de autenticación y adaptarla al modelo de
 * seguridad utilizado por Spring Security.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Repositorio utilizado para recuperar los usuarios activos almacenados
     * en la base de datos.
     */
    private final UsuarioRepository usuarioRepository;
    /**
     * Carga un usuario activo a partir de su nombre de usuario.
     *
     * Recupera el usuario desde la base de datos y construye un objeto
     * {@link UserDetails} con sus credenciales y autoridades para que
     * Spring Security pueda realizar el proceso de autenticación.
     *
     * @param username nombre de usuario utilizado para iniciar sesión.
     * @return información del usuario adaptada al modelo de Spring Security.
     * @throws UsernameNotFoundException si no existe un usuario activo con
     * el nombre indicado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsernameAndActivoTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        Set<SimpleGrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()));

        return new User(
                usuario.getUsername(),
                usuario.getPassword(),
                authorities
        );
    }
}