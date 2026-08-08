package com.medisync.api.mapper;

import com.medisync.api.dto.response.EmpleadoResponse;
import com.medisync.api.entity.Empleado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper encargado de convertir entidades {@link Empleado}
 * en objetos de respuesta {@link EmpleadoResponse}.
 *
 * Utiliza {@link EspecialidadMapper} para transformar las especialidades
 * asociadas al empleado en sus correspondientes DTOs de respuesta.
 *
 * De esta forma se evita exponer directamente las entidades JPA
 * a través de la API REST.
 */
@Component
@RequiredArgsConstructor
public class EmpleadoMapper {

    /**
     * Mapper utilizado para convertir las especialidades asociadas
     * al empleado en DTOs de respuesta.
     */
    private final EspecialidadMapper especialidadMapper;

    /**
     * Convierte una entidad Empleado en un DTO de respuesta.
     *
     * Obtiene el nombre y el email desde la Persona asociada,
     * transforma el rol a String y convierte la lista de especialidades
     * mediante {@link EspecialidadMapper}.
     *
     * @param empleado entidad Empleado que se desea convertir.
     * @return DTO con la información del empleado y sus especialidades.
     */
    public EmpleadoResponse toResponse(Empleado empleado) {
        return EmpleadoResponse.builder()
                .id(empleado.getId())
                .nombre(empleado.getPersona().getNombre())
                .email(empleado.getPersona().getEmail())
                .rol(empleado.getRol().name())
                .especialidades(especialidadMapper.toResponseList(empleado.getEspecialidades()))
                .build();
    }

    /**
     * Convierte una lista de entidades Empleado en una lista
     * de DTOs de respuesta.
     *
     * Utiliza Streams y {@code map()} para aplicar la conversión
     * a cada empleado de la lista.
     *
     * @param empleados lista de empleados que se desea convertir.
     * @return lista de EmpleadoResponse.
     */
    public List<EmpleadoResponse> toResponseList(List<Empleado> empleados) {
        return empleados.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}