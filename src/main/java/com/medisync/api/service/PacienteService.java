package com.medisync.api.service;

import com.medisync.api.dto.response.PacienteResponse;

import java.util.List;

/**
 * Define las operaciones relacionadas con la gestión de pacientes.
 *
 * Establece el contrato de la capa de servicio para consultar
 * y realizar operaciones sobre los pacientes activos del sistema.
 */
public interface PacienteService {

    /**
     * Obtiene todos los pacientes activos.
     *
     * @return lista de pacientes representados mediante DTOs de respuesta.
     */
    List<PacienteResponse> getAllPacientes();

    /**
     * Obtiene un paciente activo mediante su identificador.
     *
     * @param id identificador del paciente.
     * @return información del paciente solicitado.
     */
    PacienteResponse getPacienteById(Long id);

    /**
     * Realiza el borrado lógico de un paciente.
     *
     * El paciente no se elimina físicamente de la base de datos,
     * sino que se marca como inactivo.
     *
     * @param id identificador del paciente que se desea eliminar.
     */
    void deletePaciente(Long id);
}