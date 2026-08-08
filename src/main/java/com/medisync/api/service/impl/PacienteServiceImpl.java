package com.medisync.api.service.impl;

import com.medisync.api.dto.response.PacienteResponse;
import com.medisync.api.exception.ResourceNotFoundException;
import com.medisync.api.mapper.PacienteMapper;
import com.medisync.api.repository.PacienteRepository;
import com.medisync.api.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación de la lógica de negocio relacionada con los pacientes.
 *
 * Gestiona las operaciones de consulta y eliminación de pacientes,
 * delegando el acceso a datos en {@link PacienteRepository} y la
 * conversión entre entidades y DTOs en {@link PacienteMapper}.
 *
 * Aplica borrado lógico (Soft Delete) mediante el campo {@code activo},
 * evitando eliminar físicamente los registros de la base de datos.
 */
@Service
@RequiredArgsConstructor
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;

    /**
     * Obtiene todos los pacientes activos.
     *
     * Recupera únicamente los pacientes cuyo campo {@code activo}
     * se encuentra establecido a {@code true} y los convierte a
     * objetos {@link PacienteResponse}.
     *
     * @return lista de pacientes activos.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> getAllPacientes() {
        List<com.medisync.api.entity.Paciente> pacientes =
                pacienteRepository.findAllByActivoTrue();

        return pacienteMapper.toResponseList(pacientes);
    }

    /**
     * Obtiene un paciente activo mediante su identificador.
     *
     * Si no existe un paciente activo con el ID indicado, se lanza
     * una excepción {@link ResourceNotFoundException}.
     *
     * @param id identificador del paciente.
     * @return información del paciente solicitado.
     * @throws ResourceNotFoundException si el paciente no existe
     *         o se encuentra inactivo.
     */
    @Override
    @Transactional(readOnly = true)
    public PacienteResponse getPacienteById(Long id) {
        com.medisync.api.entity.Paciente paciente =
                pacienteRepository.findByIdAndActivoTrue(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Paciente no encontrado con id: " + id));

        return pacienteMapper.toResponse(paciente);
    }

    /**
     * Realiza el borrado lógico de un paciente.
     *
     * En lugar de eliminar físicamente el registro de la base de datos,
     * establece el campo {@code activo} a {@code false}.
     *
     * Además, si el paciente tiene un usuario asociado, también se
     * desactiva dicho usuario para impedir que pueda autenticarse.
     *
     * @param id identificador del paciente.
     * @throws ResourceNotFoundException si el paciente no existe
     *         o se encuentra inactivo.
     */
    @Override
    @Transactional
    public void deletePaciente(Long id) {
        com.medisync.api.entity.Paciente paciente =
                pacienteRepository.findByIdAndActivoTrue(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Paciente no encontrado con id: " + id));

        // Borrado lógico del paciente
        paciente.setActivo(false);

        // Desactivar también el usuario asociado, si existe
        if (paciente.getPersona() != null
                && paciente.getPersona().getUsuario() != null) {
            paciente.getPersona().getUsuario().setActivo(false);
        }

        pacienteRepository.save(paciente);
    }
}