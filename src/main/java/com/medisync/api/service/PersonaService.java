package com.medisync.api.service;

import com.medisync.api.dto.request.PersonaRequest;
import com.medisync.api.dto.response.PersonaResponse;

/**
 * Interfaz que define las operaciones de la capa de servicio
 * relacionadas con la gestión de personas.
 *
 * Actúa como contrato entre la capa de presentación y la implementación
 * de la lógica de negocio.
 */
public interface PersonaService {

    /**
     * Crea una nueva persona a partir de los datos recibidos.
     *
     * @param personaRequest DTO con los datos de la persona.
     * @return DTO con los datos de la persona creada.
     */
    PersonaResponse createPersona(PersonaRequest personaRequest);
}
