package com.medisync.api.mapper;

import com.medisync.api.dto.request.PersonaRequest;
import com.medisync.api.dto.response.PersonaResponse;
import com.medisync.api.entity.Persona;
import org.springframework.stereotype.Component;

/**
 * Componente encargado de realizar las conversiones entre la entidad
 * Persona y sus correspondientes DTOs de entrada y salida.
 *
 * Permite mantener separada la entidad de persistencia de los objetos
 * utilizados en la comunicación de la API.
 */
@Component
public class PersonaMapper {

    /**
     * Convierte un {@link PersonaRequest} en una entidad {@link Persona}.
     *
     * Utiliza el patrón Builder para construir la entidad a partir
     * de los datos recibidos en la petición.
     *
     * @param personaRequest DTO con los datos de la persona.
     * @return entidad Persona construida a partir del DTO.
     */
    public Persona toEntity(PersonaRequest personaRequest) {
        return Persona.builder()
                .nombre(personaRequest.getNombre())
                .email(personaRequest.getEmail())
                .build();
    }

    /**
     * Convierte una entidad {@link Persona} en un {@link PersonaResponse}.
     *
     * Se utiliza para transformar los datos de persistencia en el objeto
     * que será devuelto por la API.
     *
     * @param persona entidad Persona que se desea convertir.
     * @return DTO de respuesta con los datos de la persona.
     */
    public PersonaResponse toResponse(Persona persona) {
        return PersonaResponse.builder()
                .id(persona.getId())
                .nombre(persona.getNombre())
                .email(persona.getEmail())
                .build();
    }
}
