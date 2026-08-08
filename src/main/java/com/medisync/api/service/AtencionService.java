package com.medisync.api.service;

import com.medisync.api.dto.request.AtencionCreateRequest;
import com.medisync.api.dto.request.AtencionUpdateRequest;
import com.medisync.api.dto.response.AtencionResponse;
import com.medisync.api.dto.response.EstadisticaAtencionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Define las operaciones de negocio relacionadas con la gestión de
 * las atenciones médicas.
 *
 * Establece el contrato de la capa de servicio para la creación,
 * actualización, consulta y eliminación de atenciones, así como para
 * la obtención de estadísticas asociadas a los empleados.
 */
public interface AtencionService {
    /**
     * Registra una nueva atención médica.
     *
     * @param atencionCreateRequest datos necesarios para crear la atención.
     * @return información de la atención creada.
     */
    AtencionResponse createAtencion(AtencionCreateRequest atencionCreateRequest);
    /**
     * Actualiza una atención médica existente.
     *
     * @param id identificador de la atención que se desea actualizar.
     * @param atencionUpdateRequest datos que se utilizarán para actualizar
     * la atención.
     * @return información actualizada de la atención.
     */
    AtencionResponse updateAtencion(Long id, AtencionUpdateRequest atencionUpdateRequest);
    /**
     * Elimina una atención médica.
     *
     * @param id identificador de la atención que se desea eliminar.
     */
    void deleteAtencion(Long id);
    /**
     * Obtiene todas las atenciones médicas activas de forma paginada.
     *
     * @param pageable información de paginación y ordenación.
     * @return página con las atenciones médicas.
     */
    Page<AtencionResponse> getAllAtenciones(Pageable pageable);
    /**
     * Obtiene las atenciones asociadas al paciente autenticado.
     *
     * @param username nombre de usuario del paciente autenticado.
     * @return lista de atenciones correspondientes al paciente.
     */
    List<AtencionResponse> getMyAtenciones(String username);
    /**
     * Busca atenciones médicas aplicando filtros opcionales por empleado
     * y rango de fechas.
     *
     * @param empleadoId identificador del empleado responsable de las
     * atenciones.
     * @param startDate fecha y hora inicial del rango de búsqueda.
     * @param endDate fecha y hora final del rango de búsqueda.
     * @return lista de atenciones que cumplen los filtros especificados.
     */
    List<AtencionResponse> getAtencionesByFiltros(Long empleadoId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Obtiene las estadísticas de atención correspondientes a un empleado.
     *
     * @param empleadoId identificador del empleado.
     * @return estadísticas de las atenciones asociadas al empleado.
     */
    EstadisticaAtencionResponse getEstadisticasByEmpleado(Long empleadoId);
}