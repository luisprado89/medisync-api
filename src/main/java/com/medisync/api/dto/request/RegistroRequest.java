package com.medisync.api.dto.request;

import com.medisync.api.enums.RolUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

/**
 * DTO utilizado para recibir los datos necesarios durante el registro
 * de un nuevo usuario en el sistema.
 *
 * Contiene las credenciales de acceso, el rol que tendrá el usuario
 * y el identificador de la persona a la que quedará asociado.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RegistroRequest {

    /**
     * Nombre de usuario utilizado para iniciar sesión.
     */
    @NotBlank(message = "El username es obligatorio")
    private String username;

    /**
     * Contraseña proporcionada durante el registro.
     *
     * La contraseña recibida no debe almacenarse directamente en la base
     * de datos, sino que debe ser cifrada mediante PasswordEncoder.
     */
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    /**
     * Rol que tendrá el usuario dentro del sistema.
     */
    @NotNull(message = "El rol es obligatorio")
    private RolUsuario rol;

    /**
     * Identificador de la persona que quedará asociada al usuario.
     */
    @NotNull(message = "El ID de la persona es obligatorio")
    @Positive(message = "El ID de la persona debe ser positivo")
    private Long personaId;
}
