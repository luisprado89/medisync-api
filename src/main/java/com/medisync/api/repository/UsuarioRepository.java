package com.medisync.api.repository;

import com.medisync.api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio encargado de gestionar las operaciones de acceso a datos
 * relacionadas con la entidad {@link Usuario}.
 *
 * Extiende {@link JpaRepository}, proporcionando las operaciones CRUD
 * básicas y permitiendo definir consultas derivadas mediante el nombre
 * de los métodos.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario activo a partir de su nombre de usuario.
     *
     * Este método se utiliza durante el proceso de autenticación para
     * recuperar únicamente usuarios que se encuentren activos.
     *
     * @param username nombre de usuario que se desea buscar.
     * @return un {@link Optional} que contiene el usuario si existe y está
     * activo; en caso contrario, un {@link Optional#empty()}.
     */
    Optional<Usuario> findByUsernameAndActivoTrue(String username);
}