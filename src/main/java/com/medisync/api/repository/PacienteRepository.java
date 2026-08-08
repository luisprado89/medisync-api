package com.medisync.api.repository;

import com.medisync.api.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio encargado de gestionar las operaciones de acceso a datos
 * relacionadas con la entidad {@link Paciente}.
 *
 * Extiende {@link JpaRepository}, proporcionando las operaciones CRUD
 * básicas y permitiendo definir consultas derivadas mediante el nombre
 * de los métodos.
 */
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    /**
     * Busca un paciente activo a partir del identificador de la persona
     * asociada.
     *
     * Este método permite localizar el registro de un paciente utilizando
     * la relación existente con la entidad {@link com.medisync.api.entity.Persona},
     * considerando únicamente aquellos pacientes que se encuentren activos.
     *
     * @param personaId identificador de la persona asociada al paciente.
     * @return un {@link Optional} que contiene el paciente si existe y está
     * activo; en caso contrario, un {@link Optional#empty()}.
     */
    Optional<Paciente> findByPersonaIdAndActivoTrue(Long personaId);
    /**
     * Obtiene todos los pacientes activos.
     *
     * @return lista de pacientes activos.
     */
    List<Paciente> findAllByActivoTrue();

    /**
     * Busca un paciente por su identificador comprobando que esté activo.
     *
     * @param id identificador del paciente.
     * @return el paciente activo encontrado, si existe.
     */
    Optional<Paciente> findByIdAndActivoTrue(Long id);
}