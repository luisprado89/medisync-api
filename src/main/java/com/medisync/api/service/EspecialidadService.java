package com.medisync.api.service;

import com.medisync.api.dto.request.EspecialidadRequest;
import com.medisync.api.dto.response.EspecialidadResponse;

import java.util.List;

/**
 * Interfaz que define las operaciones de la capa de servicio
 * relacionadas con la gestión de especialidades médicas.
 *
 * Establece el contrato para las operaciones de creación, actualización,
 * eliminación, consulta y búsqueda de especialidades.
 */
public interface EspecialidadService {

    /**
     * Crea una nueva especialidad médica.
     *
     * @param especialidadRequest DTO con los datos de la especialidad.
     * @return DTO con la especialidad creada.
     */
    EspecialidadResponse createEspecialidad(EspecialidadRequest especialidadRequest);

    /**
     * Actualiza una especialidad existente.
     *
     * @param id identificador de la especialidad que se desea actualizar.
     * @param especialidadRequest DTO con los nuevos datos de la especialidad.
     * @return DTO con la especialidad actualizada.
     */
    EspecialidadResponse updateEspecialidad(Long id, EspecialidadRequest especialidadRequest);

    /**
     * Realiza el borrado lógico de una especialidad.
     *
     * @param id identificador de la especialidad que se desea eliminar.
     */
    void deleteEspecialidad(Long id);

    /**
     * Obtiene una especialidad mediante su identificador.
     *
     * @param id identificador de la especialidad.
     * @return DTO con los datos de la especialidad encontrada.
     */
    EspecialidadResponse getEspecialidadById(Long id);

    /**
     * Obtiene todas las especialidades activas.
     *
     * @return lista de DTOs con las especialidades disponibles.
     */
    List<EspecialidadResponse> getAllEspecialidades();

    /**
     * Busca especialidades activas por nombre.
     *
     * @param nombre texto utilizado como criterio de búsqueda.
     * @return lista de especialidades que coinciden con el nombre indicado.
     */
    List<EspecialidadResponse> searchEspecialidadesByNombre(String nombre);
}
