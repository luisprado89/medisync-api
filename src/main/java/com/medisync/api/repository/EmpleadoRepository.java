package com.medisync.api.repository;

import com.medisync.api.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio encargado de gestionar las operaciones de acceso a datos
 * relacionadas con la entidad {@link Empleado}.
 *
 * Extiende {@link JpaRepository}, proporcionando las operaciones CRUD
 * básicas y permitiendo definir consultas derivadas mediante el nombre
 * de los métodos.
 */
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    /**
     * Busca un empleado activo a partir del identificador de la persona
     * asociada.
     *
     * Este método permite localizar el registro de un empleado utilizando
     * la relación existente con la entidad {@link com.medisync.api.entity.Persona},
     * considerando únicamente aquellos empleados que se encuentren activos.
     *
     * @param personaId identificador de la persona asociada al empleado.
     * @return un {@link Optional} que contiene el empleado si existe y está
     * activo; en caso contrario, un {@link Optional#empty()}.
     */
    Optional<Empleado> findByPersonaIdAndActivoTrue(Long personaId);

    /**
     * Obtiene todos los empleados activos.
     *
     * @return lista de empleados activos.
     */
    List<Empleado> findAllByActivoTrue();

    /**
     * Busca un empleado por su identificador comprobando que esté activo.
     *
     * @param id identificador del empleado.
     * @return el empleado activo encontrado, si existe.
     */
    Optional<Empleado> findByIdAndActivoTrue(Long id);
}