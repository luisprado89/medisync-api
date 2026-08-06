package com.medisync.api.repository;

import com.medisync.api.entity.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio encargado de gestionar las operaciones de acceso a datos
 * relacionadas con la entidad {@link Especialidad}.
 *
 * Extiende {@link JpaRepository}, proporcionando las operaciones CRUD
 * básicas sobre las especialidades médicas, así como las funcionalidades
 * de consulta y persistencia ofrecidas por Spring Data JPA.
 */
public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
}