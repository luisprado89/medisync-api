package com.medisync.api.service.impl;

import com.medisync.api.dto.request.EspecialidadRequest;
import com.medisync.api.dto.response.EspecialidadResponse;
import com.medisync.api.entity.Especialidad;
import com.medisync.api.exception.BusinessRuleException;
import com.medisync.api.exception.ResourceNotFoundException;
import com.medisync.api.mapper.EspecialidadMapper;
import com.medisync.api.repository.EspecialidadRepository;
import com.medisync.api.service.EspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación de la interfaz {@link EspecialidadService} encargada de gestionar
 * la lógica de negocio relacionada con las especialidades médicas.
 *
 * Coordina el acceso al repositorio y la conversión entre entidades y DTOs
 * mediante {@link EspecialidadMapper}.
 */
@Service
@RequiredArgsConstructor
public class EspecialidadServiceImpl implements EspecialidadService {

    private final EspecialidadRepository especialidadRepository;
    private final EspecialidadMapper especialidadMapper;

    /**
     * Crea una nueva especialidad médica.
     *
     * Antes de realizar la inserción, comprueba que no exista otra
     * especialidad activa con el mismo nombre.
     *
     * @param especialidadRequest DTO con los datos de la especialidad.
     * @return DTO con la especialidad creada.
     * @throws BusinessRuleException si ya existe una especialidad activa con el mismo nombre.
     */
    @Override
    @Transactional
    public EspecialidadResponse createEspecialidad(EspecialidadRequest especialidadRequest) {
        // Validar que no exista ya una especialidad con ese nombre
        especialidadRepository.findByNombreIgnoreCaseAndActivoTrue(especialidadRequest.getNombre())
                .ifPresent(e -> {
                    throw new BusinessRuleException(
                            "La especialidad '" + especialidadRequest.getNombre() + "' ya existe."
                    );
                });

        Especialidad especialidad = especialidadMapper.toEntity(especialidadRequest);
        especialidad.setActivo(true);

        Especialidad saved = especialidadRepository.save(especialidad);
        return especialidadMapper.toResponse(saved);
    }

    /**
     * Actualiza una especialidad existente.
     *
     * Solo permite modificar especialidades activas.
     *
     * @param id identificador de la especialidad que se desea actualizar.
     * @param especialidadRequest DTO con los nuevos datos.
     * @return DTO con la especialidad actualizada.
     * @throws ResourceNotFoundException si la especialidad no existe o se encuentra inactiva.
     */
    @Override
    @Transactional
    public EspecialidadResponse updateEspecialidad(Long id, EspecialidadRequest especialidadRequest) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .filter(Especialidad::getActivo)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Especialidad no encontrada con id: " + id
                        )
                );

        especialidadMapper.updateEntityFromRequest(especialidadRequest, especialidad);

        Especialidad updated = especialidadRepository.save(especialidad);
        return especialidadMapper.toResponse(updated);
    }

    /**
     * Realiza el borrado lógico de una especialidad.
     *
     * En lugar de eliminar físicamente el registro de la base de datos,
     * establece el campo {@code activo} a {@code false}.
     *
     * @param id identificador de la especialidad que se desea eliminar.
     * @throws ResourceNotFoundException si la especialidad no existe o ya se encuentra inactiva.
     */
    @Override
    @Transactional
    public void deleteEspecialidad(Long id) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .filter(Especialidad::getActivo)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Especialidad no encontrada con id: " + id
                        )
                );

        especialidad.setActivo(false); // Soft Delete
        especialidadRepository.save(especialidad);
    }

    /**
     * Obtiene una especialidad activa mediante su identificador.
     *
     * @param id identificador de la especialidad.
     * @return DTO con los datos de la especialidad.
     * @throws ResourceNotFoundException si la especialidad no existe  o se encuentra inactiva.
     */
    @Override
    @Transactional(readOnly = true)
    public EspecialidadResponse getEspecialidadById(Long id) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .filter(Especialidad::getActivo)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Especialidad no encontrada con id: " + id
                        )
                );

        return especialidadMapper.toResponse(especialidad);
    }

    /**
     * Obtiene todas las especialidades activas.
     *
     * @return lista de DTOs con las especialidades activas.
     */
    @Override
    @Transactional(readOnly = true)
    public List<EspecialidadResponse> getAllEspecialidades() {
        List<Especialidad> especialidades =
                especialidadRepository.findAllByActivoTrue();

        return especialidadMapper.toResponseList(especialidades);
    }

    /**
     * Busca especialidades activas cuyo nombre contenga el texto indicado,
     * ignorando diferencias entre mayúsculas y minúsculas.
     *
     * @param nombre texto utilizado como criterio de búsqueda.
     * @return lista de DTOs con las especialidades que coinciden con la búsqueda.
     */
    @Override
    @Transactional(readOnly = true)
    public List<EspecialidadResponse> searchEspecialidadesByNombre(String nombre) {
        List<Especialidad> especialidades =
                especialidadRepository.findByNombreContainingIgnoreCaseAndActivoTrue(nombre);

        return especialidadMapper.toResponseList(especialidades);
    }
}
