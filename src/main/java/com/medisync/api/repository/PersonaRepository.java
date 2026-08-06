package com.medisync.api.repository;

import com.medisync.api.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio encargado de gestionar las operaciones de acceso a datos
 * relacionadas con la entidad {@link Persona}.
 *
 * Extiende {@link JpaRepository}, proporcionando las operaciones CRUD
 * básicas y permitiendo definir consultas derivadas mediante el nombre
 * de los métodos.
 */
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    /**
     * Busca una persona activa a partir de su dirección de correo electrónico.
     *
     * Este método permite recuperar únicamente registros que se encuentren
     * activos, garantizando que no se tengan en cuenta personas inactivas.
     *
     * @param email dirección de correo electrónico de la persona.
     * @return un {@link Optional} que contiene la persona si existe y está
     * activa; en caso contrario, un {@link Optional#empty()}.
     */
    Optional<Persona> findByEmailAndActivoTrue(String email);
}