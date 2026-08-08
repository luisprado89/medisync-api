package com.medisync.api.repository;

import com.medisync.api.entity.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
/**
 * Repositorio encargado de gestionar las operaciones de acceso a datos
 * relacionadas con la entidad {@link Especialidad}.
 *
 * Extiende {@link JpaRepository}, proporcionando las operaciones CRUD
 * básicas sobre las especialidades médicas, así como las funcionalidades
 * de consulta y persistencia ofrecidas por Spring Data JPA.
 */
public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {

    /**
     * Busca una especialidad activa por su nombre, ignorando las diferencias
     * entre mayúsculas y minúsculas.
     *
     * @param nombre nombre de la especialidad que se desea buscar.
     * @return {@link Optional} que contiene la especialidad encontrada,
     *  o vacío si no existe una especialidad activa con ese nombre.
     */
    Optional<Especialidad> findByNombreIgnoreCaseAndActivoTrue(String nombre);

    /**
     * Busca especialidades activas cuyo nombre contenga el texto indicado,
     * ignorando las diferencias entre mayúsculas y minúsculas.
     *
     * @param nombre texto que debe estar contenido en el nombre de la especialidad.
     * @return lista de especialidades activas que coinciden con el criterio de búsqueda.
     */
    List<Especialidad> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);

    /**
     * Obtiene todas las especialidades activas.
     *
     * @return lista de especialidades cuyo campo {@code activo} es {@code true}.
     */
    List<Especialidad> findAllByActivoTrue();
}
