package com.medisync.api.service.impl;

import com.medisync.api.dto.response.EmpleadoResponse;
import com.medisync.api.entity.Empleado;
import com.medisync.api.exception.ResourceNotFoundException;
import com.medisync.api.mapper.EmpleadoMapper;
import com.medisync.api.repository.EmpleadoRepository;
import com.medisync.api.service.EmpleadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación de la lógica de negocio relacionada con los empleados.
 *
 * Gestiona las operaciones de consulta y eliminación de empleados,
 * delegando el acceso a datos en {@link EmpleadoRepository} y la
 * conversión entre entidades y DTOs en {@link EmpleadoMapper}.
 *
 * Aplica borrado lógico (Soft Delete) mediante el campo {@code activo},
 * evitando eliminar físicamente los registros de la base de datos.
 */
@Service
@RequiredArgsConstructor
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoMapper empleadoMapper;

    /**
     * Obtiene todos los empleados activos.
     *
     * Recupera únicamente los empleados cuyo campo {@code activo}
     * se encuentra establecido a {@code true} y los convierte a
     * objetos {@link EmpleadoResponse}.
     *
     * @return lista de empleados activos.
     */
    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoResponse> getAllEmpleados() {
        List<Empleado> empleados = empleadoRepository.findAllByActivoTrue();

        return empleadoMapper.toResponseList(empleados);
    }

    /**
     * Obtiene un empleado activo mediante su identificador.
     *
     * Si no existe un empleado activo con el ID indicado, se lanza
     * una excepción {@link ResourceNotFoundException}.
     *
     * @param id identificador del empleado.
     * @return información del empleado solicitado.
     * @throws ResourceNotFoundException si el empleado no existe
     *         o se encuentra inactivo.
     */
    @Override
    @Transactional(readOnly = true)
    public EmpleadoResponse getEmpleadoById(Long id) {
        Empleado empleado = empleadoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Empleado no encontrado con id: " + id));

        return empleadoMapper.toResponse(empleado);
    }

    /**
     * Realiza el borrado lógico de un empleado.
     *
     * En lugar de eliminar físicamente el registro de la base de datos,
     * establece el campo {@code activo} a {@code false}.
     *
     * Además, si el empleado tiene un usuario asociado, también se
     * desactiva dicho usuario para impedir que pueda autenticarse.
     *
     * @param id identificador del empleado.
     * @throws ResourceNotFoundException si el empleado no existe
     *         o se encuentra inactivo.
     */
    @Override
    @Transactional
    public void deleteEmpleado(Long id) {
        Empleado empleado = empleadoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Empleado no encontrado con id: " + id));

        // Borrado lógico del empleado
        empleado.setActivo(false);

        // Desactivar también el usuario asociado, si existe
        if (empleado.getPersona() != null
                && empleado.getPersona().getUsuario() != null) {
            empleado.getPersona().getUsuario().setActivo(false);
        }

        empleadoRepository.save(empleado);
    }
}