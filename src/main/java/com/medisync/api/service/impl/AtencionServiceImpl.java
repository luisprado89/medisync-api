package com.medisync.api.service.impl;

import com.medisync.api.dto.request.AtencionCreateRequest;
import com.medisync.api.dto.request.AtencionUpdateRequest;
import com.medisync.api.dto.response.AtencionResponse;
import com.medisync.api.dto.response.EstadisticaAtencionResponse;
import com.medisync.api.entity.Atencion;
import com.medisync.api.entity.Empleado;
import com.medisync.api.entity.Paciente;
import com.medisync.api.entity.Usuario;
import com.medisync.api.exception.BusinessRuleException;
import com.medisync.api.exception.ResourceNotFoundException;
import com.medisync.api.mapper.AtencionMapper;
import com.medisync.api.repository.AtencionRepository;
import com.medisync.api.repository.EmpleadoRepository;
import com.medisync.api.repository.PacienteRepository;
import com.medisync.api.repository.UsuarioRepository;
import com.medisync.api.service.AtencionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementación del servicio encargado de gestionar las atenciones médicas.
 *
 * Coordina las operaciones de acceso a datos, transformación de entidades
 * mediante el mapper y aplicación de las reglas de negocio relacionadas
 * con las atenciones.
 */
@Service
@RequiredArgsConstructor
public class AtencionServiceImpl implements AtencionService {

    /**
     * Repositorio utilizado para gestionar las atenciones médicas.
     */
    private final AtencionRepository atencionRepository;
    /**
     * Repositorio utilizado para consultar los pacientes.
     */
    private final PacienteRepository pacienteRepository;
    /**
     * Repositorio utilizado para consultar los empleados.
     */
    private final EmpleadoRepository empleadoRepository;
    /**
     * Repositorio utilizado para consultar los usuarios.
     */
    private final UsuarioRepository usuarioRepository;
    /**
     * Componente encargado de transformar entidades de atención
     * en DTOs y viceversa.
     */
    private final AtencionMapper atencionMapper;
    /**
     * Registra una nueva atención médica.
     *
     * Comprueba que el paciente y el empleado asociados existan y se
     * encuentren activos antes de crear y persistir la atención.
     *
     * @param atencionCreateRequest datos necesarios para crear la atención.
     * @return información de la atención creada.
     * @throws ResourceNotFoundException si el paciente o empleado no existe
     * o se encuentra inactivo.
     */
    @Override
    @Transactional
    public AtencionResponse createAtencion(AtencionCreateRequest atencionCreateRequest) {
        Paciente paciente = pacienteRepository.findById(atencionCreateRequest.getPacienteId())
                .filter(Paciente::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + atencionCreateRequest.getPacienteId()));

        Empleado empleado = empleadoRepository.findById(atencionCreateRequest.getEmpleadoId())
                .filter(Empleado::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + atencionCreateRequest.getEmpleadoId()));

        Atencion atencion = atencionMapper.toEntity(atencionCreateRequest, paciente, empleado);
        atencion.setActivo(true);

        Atencion savedAtencion = atencionRepository.save(atencion);
        return atencionMapper.toResponse(savedAtencion);
    }

    /**
     * Actualiza una atención médica existente.
     *
     * Solo permite modificar atenciones que se encuentren activas y aplica
     * únicamente los campos proporcionados en la solicitud.
     *
     * @param id identificador de la atención que se desea actualizar.
     * @param request datos utilizados para actualizar la atención.
     * @return información actualizada de la atención.
     * @throws ResourceNotFoundException si la atención no existe o se
     * encuentra inactiva.
     */
    @Override
    @Transactional
    public AtencionResponse updateAtencion(Long id, AtencionUpdateRequest request) {
        Atencion atencion = atencionRepository.findById(id)
                .filter(Atencion::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Atención no encontrada con id: " + id));

        atencionMapper.updateEntityFromRequest(request, atencion);
        Atencion updatedAtencion = atencionRepository.save(atencion);
        return atencionMapper.toResponse(updatedAtencion);
    }

    /**
     * Realiza el borrado lógico de una atención médica.
     *
     * En lugar de eliminar físicamente el registro de la base de datos,
     * establece su estado como inactivo para conservar la información.
     *
     * @param id identificador de la atención que se desea eliminar.
     * @throws ResourceNotFoundException si la atención no existe o ya
     * se encuentra inactiva.
     */
    @Override
    @Transactional
    public void deleteAtencion(Long id) {
        Atencion atencion = atencionRepository.findById(id)
                .filter(Atencion::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Atención no encontrada con id: " + id));

        // Borrado lógico (Soft Delete)
        atencion.setActivo(false);
        atencionRepository.save(atencion);
    }

    /**
     * Obtiene todas las atenciones médicas activas de forma paginada.
     *
     * @param pageable configuración de paginación y ordenación.
     * @return página de atenciones transformadas a DTOs de respuesta.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AtencionResponse> getAllAtenciones(Pageable pageable) {
        Page<Atencion> atenciones = atencionRepository.findAllByActivoTrue(pageable);
        return atenciones.map(atencionMapper::toResponse);
    }

    /**
     * Obtiene las atenciones médicas asociadas al paciente autenticado.
     *
     * Recupera el usuario a partir de su nombre de usuario y localiza
     * posteriormente el perfil de paciente asociado a su persona.
     *
     * @param username nombre de usuario del paciente autenticado.
     * @return lista de atenciones pertenecientes al paciente.
     * @throws ResourceNotFoundException si el usuario no existe.
     * @throws BusinessRuleException si el usuario no tiene un perfil
     * de paciente asociado.
     */
    @Override
    @Transactional(readOnly = true)
    public List<AtencionResponse> getMyAtenciones(String username) {
        Usuario usuario = usuarioRepository.findByUsernameAndActivoTrue(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));

        Paciente paciente = pacienteRepository.findByPersonaIdAndActivoTrue(usuario.getPersona().getId())
                .orElseThrow(() -> new BusinessRuleException("El usuario no tiene perfil de paciente asociado."));

        List<Atencion> atenciones = atencionRepository.findAllByPacienteIdAndActivoTrue(paciente.getId());
        return atencionMapper.toResponseList(atenciones);
    }
    /**
     * Busca atenciones médicas aplicando filtros opcionales por empleado
     * y rango de fechas.
     *
     * @param empleadoId identificador del empleado responsable; puede ser
     * {@code null} para no aplicar este filtro.
     * @param startDate fecha y hora inicial del rango; puede ser
     * {@code null}.
     * @param endDate fecha y hora final del rango; puede ser
     * {@code null}.
     * @return lista de atenciones que cumplen los filtros indicados.
     */
    @Override
    @Transactional(readOnly = true)
    public List<AtencionResponse> getAtencionesByFiltros(Long empleadoId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Atencion> atenciones = atencionRepository.findAtencionesByFiltros(empleadoId, startDate, endDate);
        return atencionMapper.toResponseList(atenciones);
    }

    /**
     * Calcula las estadísticas de las atenciones correspondientes a
     * un empleado.
     *
     * Calcula el total de ingresos, el número de atenciones y la
     * distribución de las atenciones agrupadas por estado.
     *
     * Si el empleado no tiene atenciones, devuelve estadísticas con
     * valores iniciales a cero y un mapa vacío.
     *
     * @param empleadoId identificador del empleado.
     * @return estadísticas agregadas de las atenciones del empleado.
     * @throws ResourceNotFoundException si el empleado no existe o
     * se encuentra inactivo.
     */
    @Override
    @Transactional(readOnly = true)
    public EstadisticaAtencionResponse getEstadisticasByEmpleado(Long empleadoId) {
        Empleado empleado = empleadoRepository.findById(empleadoId)
                .filter(Empleado::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + empleadoId));

        List<Atencion> atenciones = atencionRepository.findAtencionesByFiltros(empleadoId, null, null);

        if (atenciones.isEmpty()) {
            return EstadisticaAtencionResponse.builder()
                    .empleadoNombre(empleado.getPersona().getNombre())
                    .totalIngresos(BigDecimal.ZERO)
                    .cantidadAtenciones(0L)
                    .atencionesPorEstado(Map.of())
                    .build();
        }

        // 1. REDUCCIÓN: Sumar todos los importes (dinero generado por el médico)
        BigDecimal totalIngresos = atenciones.stream()
                .map(Atencion::getImporte)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2. AGRUPACIÓN Y ESTADÍSTICA: Agrupar por estado y contar cuántas hay de cada una
        Map<String, Long> atencionesPorEstado = atenciones.stream()
                .collect(Collectors.groupingBy(a -> a.getEstado().name(), Collectors.counting()));

        return EstadisticaAtencionResponse.builder()
                .empleadoNombre(empleado.getPersona().getNombre())
                .totalIngresos(totalIngresos)
                .cantidadAtenciones((long) atenciones.size())
                .atencionesPorEstado(atencionesPorEstado)
                .build();
    }
}