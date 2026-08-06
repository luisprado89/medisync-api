package com.medisync.api.repository;

import com.medisync.api.entity.Atencion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio encargado de gestionar las operaciones de acceso a datos
 * relacionadas con la entidad {@link Atencion}.
 *
 * Extiende {@link JpaRepository}, proporcionando las operaciones CRUD
 * básicas y definiendo consultas específicas para la gestión de las
 * atenciones médicas.
 */
public interface AtencionRepository extends JpaRepository<Atencion, Long> {

    /**
     * Obtiene de forma paginada todas las atenciones activas.
     *
     * Este método se utiliza para el listado general de atenciones,
     * permitiendo controlar el número de registros devueltos mediante
     * paginación.
     *
     * @param pageable información de paginación y ordenación.
     * @return una página con las atenciones activas.
     */
    Page<Atencion> findAllByActivoTrue(Pageable pageable);

    /**
     * Obtiene todas las atenciones activas asociadas a un paciente.
     *
     * Este método se utiliza para mostrar el historial de atenciones
     * correspondiente al paciente autenticado.
     *
     * @param pacienteId identificador del paciente.
     * @return lista de atenciones activas del paciente.
     */
    List<Atencion> findAllByPacienteIdAndActivoTrue(Long pacienteId);

    /**
     * Busca atenciones aplicando filtros opcionales por médico y rango
     * de fechas.
     *
     * La consulta permite recuperar únicamente las atenciones activas y
     * aplicar de forma dinámica los filtros recibidos. Si alguno de los
     * parámetros es {@code null}, dicho criterio no se tendrá en cuenta
     * durante la búsqueda.
     *
     * @param empleadoId identificador del médico responsable de la atención.
     * @param startDate fecha y hora mínima del intervalo de búsqueda.
     * @param endDate fecha y hora máxima del intervalo de búsqueda.
     * @return lista de atenciones que cumplen los filtros especificados.
     */
    @Query("SELECT a FROM Atencion a " +
            "JOIN FETCH a.paciente p " +
            "JOIN FETCH a.empleado e " +
            "WHERE a.activo = true " +
            "AND (:empleadoId IS NULL OR a.empleado.id = :empleadoId) " +
            "AND (:startDate IS NULL OR a.fecha >= :startDate) " +
            "AND (:endDate IS NULL OR a.fecha <= :endDate)")
    List<Atencion> findAtencionesByFiltros(@Param("empleadoId") Long empleadoId,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);
}