package com.medisync.api.mapper;

import com.medisync.api.dto.request.EspecialidadRequest;
import com.medisync.api.dto.response.EspecialidadResponse;
import com.medisync.api.entity.Especialidad;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Componente encargado de realizar las conversiones entre la entidad
 * {@link Especialidad} y los DTOs utilizados por la API.
 *
 * Centraliza el mapeo entre los objetos de entrada, las entidades
 * de persistencia y los objetos de respuesta.
 */
@Component
public class EspecialidadMapper {

    /**
     * Convierte un {@link EspecialidadRequest} en una entidad
     * {@link Especialidad}.
     *
     * @param especialidadRequest DTO con los datos de la especialidad.
     * @return entidad {@link Especialidad} construida a partir del DTO.
     */
    public Especialidad toEntity(EspecialidadRequest especialidadRequest) {
        return Especialidad.builder()
                .nombre(especialidadRequest.getNombre())
                .build();
    }

    /**
     * Actualiza los campos modificables de una entidad {@link Especialidad}
     * utilizando los valores proporcionados en el DTO de solicitud.
     *
     * Solo se actualizan los campos que no sean {@code null}.
     *
     * @param especialidadRequest DTO que contiene los nuevos datos.
     * @param especialidad entidad que será actualizada.
     */
    public void updateEntityFromRequest(EspecialidadRequest especialidadRequest, Especialidad especialidad) {
        if (especialidadRequest.getNombre() != null) {
            especialidad.setNombre(especialidadRequest.getNombre());
        }
    }

    /**
     * Convierte una entidad {@link Especialidad} en un
     * {@link EspecialidadResponse} para devolverla mediante la API.
     *
     * @param especialidad entidad que se desea convertir.
     * @return DTO de respuesta con los datos de la especialidad.
     */
    public EspecialidadResponse toResponse(Especialidad especialidad) {
        return EspecialidadResponse.builder()
                .id(especialidad.getId())
                .nombre(especialidad.getNombre())
                .build();
    }

    /**
     * Convierte una lista de entidades {@link Especialidad} en una lista
     * de {@link EspecialidadResponse} utilizando Java Streams.
     *
     * @param especialidades lista de especialidades que se desea convertir.
     * @return lista de DTOs de respuesta.
     */
    public List<EspecialidadResponse> toResponseList(List<Especialidad> especialidades) {
        return especialidades.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
