package com.medisync.api.service.impl;

import com.medisync.api.dto.request.PersonaRequest;
import com.medisync.api.dto.response.PersonaResponse;
import com.medisync.api.entity.Persona;
import com.medisync.api.exception.BusinessRuleException;
import com.medisync.api.mapper.PersonaMapper;
import com.medisync.api.repository.PersonaRepository;
import com.medisync.api.service.PersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación de la capa de servicio encargada de gestionar
 * las operaciones relacionadas con las personas.
 *
 * Contiene la lógica de negocio necesaria para crear personas,
 * incluyendo la validación de emails duplicados y la gestión
 * del estado activo de los registros.
 */
@Service
@RequiredArgsConstructor
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository personaRepository;
    private final PersonaMapper personaMapper;

    /**
     * Crea una nueva persona en el sistema.
     *
     * Antes de realizar la inserción se comprueba que no exista
     * otra persona activa con el mismo email.
     *
     * La nueva persona se crea inicialmente como activa y se
     * persiste mediante el repositorio correspondiente.
     *
     * @param personaRequest DTO que contiene los datos de la persona.
     * @return DTO con los datos de la persona creada.
     * @throws BusinessRuleException si el email ya está registrado.
     */
    @Override
    @Transactional
    public PersonaResponse createPersona(PersonaRequest personaRequest) {

        // Validar que el email no exista
        personaRepository.findByEmailAndActivoTrue(personaRequest.getEmail())
                .ifPresent(p -> {
                    throw new BusinessRuleException("El email ya está registrado");
                });

        Persona persona = personaMapper.toEntity(personaRequest);
        persona.setActivo(true);

        Persona savedPersona = personaRepository.save(persona);

        return personaMapper.toResponse(savedPersona);
    }
}
