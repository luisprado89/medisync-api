package com.medisync.api.mapper;

import com.medisync.api.dto.response.PacienteResponse;
import com.medisync.api.entity.Paciente;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper encargado de convertir entidades {@link Paciente}
 * en objetos de respuesta {@link PacienteResponse}.
 *
 * Se utiliza para separar la entidad de persistencia de los objetos
 * expuestos por la API REST.
 */
@Component
public class PacienteMapper {

    /**
     * Convierte una entidad Paciente en un DTO de respuesta.
     *
     * Obtiene el nombre y el email desde la entidad Persona asociada
     * al paciente.
     *
     * @param paciente entidad Paciente que se desea convertir.
     * @return DTO con los datos del paciente.
     */
    public PacienteResponse toResponse(Paciente paciente) {
        return PacienteResponse.builder()
                .id(paciente.getId())
                .nombre(paciente.getPersona().getNombre())
                .email(paciente.getPersona().getEmail())
                .build();
    }

    /**
     * Convierte una lista de entidades Paciente en una lista
     * de DTOs de respuesta.
     *
     * Utiliza Streams para realizar la transformación de cada elemento.
     *
     * @param pacientes lista de pacientes que se desea convertir.
     * @return lista de PacienteResponse.
     */
    public List<PacienteResponse> toResponseList(List<Paciente> pacientes) {
        return pacientes.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}