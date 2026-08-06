package com.medisync.api.config;

import com.medisync.api.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración principal de Spring Security.
 *
 * Define la cadena de filtros de seguridad de la aplicación, la política
 * de autenticación basada en JWT, los permisos de acceso a los distintos
 * endpoints y los componentes necesarios para la autenticación de usuarios.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Filtro encargado de validar los tokens JWT presentes en cada petición.
     */
    private final JwtAuthenticationFilter jwtAuthFilter;

    /**
     * Servicio utilizado por Spring Security para cargar los datos del usuario
     * durante el proceso de autenticación.
     */
    private final UserDetailsService userDetailsService;
    /**
     * Configura la cadena de filtros de seguridad de la aplicación.
     *
     * Define las rutas públicas, las restricciones de acceso según el rol del
     * usuario, la política de sesiones sin estado (JWT), el proveedor de
     * autenticación y el filtro encargado de validar los tokens JWT.
     *
     * @param http objeto utilizado para configurar la seguridad HTTP.
     * @return cadena de filtros de seguridad configurada.
     * @throws Exception si ocurre un error durante la construcción de la
     * configuración de seguridad.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/atenciones").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/atenciones/mias").hasRole("PACIENTE")
                        .requestMatchers(HttpMethod.POST, "/api/atenciones").hasAnyRole("ADMIN", "MEDICO")
                        .requestMatchers(HttpMethod.PUT, "/api/atenciones/**").hasAnyRole("ADMIN", "MEDICO")
                        .requestMatchers(HttpMethod.DELETE, "/api/atenciones/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    /**
     * Crea el codificador utilizado para almacenar y verificar contraseñas.
     *
     * @return instancia de {@link BCryptPasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /**
     * Configura el proveedor de autenticación basado en DAO.
     *
     * Utiliza el {@link UserDetailsService} para recuperar la información
     * del usuario y el {@link PasswordEncoder} para verificar las credenciales.
     *
     * @return proveedor de autenticación configurado.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    /**
     * Expone el administrador de autenticación utilizado por Spring Security.
     *
     * @param config configuración de autenticación proporcionada por Spring.
     * @return instancia del administrador de autenticación.
     * @throws Exception si no es posible obtener el administrador de autenticación.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }
}