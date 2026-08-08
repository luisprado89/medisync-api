package com.medisync.api.service;

import com.medisync.api.dto.response.EmpleadoResponse;

import java.util.List;

/**
 * Define las operaciones de la lógica de negocio relacionadas
 * con los empleados.
 *
 * Establece el contrato para consultar y gestionar los empleados
 * activos de la aplicación.
 */
public interface EmpleadoService {

    /**
     * Obtiene todos los empleados activos.
     *
     * @return lista de empleados activos.
     */
    List<EmpleadoResponse> getAllEmpleados();

    /**
     * Obtiene un empleado activo mediante su identificador.
     *
     * @param id identificador del empleado.
     * @return información del empleado solicitado.
     */
    EmpleadoResponse getEmpleadoById(Long id);

    /**
     * Realiza el borrado lógico de un empleado.
     *
     * El empleado no se elimina físicamente de la base de datos,
     * sino que se marca como inactivo mediante el campo {@code activo}.
     *
     * @param id identificador del empleado.
     */
    void deleteEmpleado(Long id);
}