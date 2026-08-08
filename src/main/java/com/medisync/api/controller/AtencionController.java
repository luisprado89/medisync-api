package com.medisync.api.controller;

import com.medisync.api.dto.request.AtencionCreateRequest;
import com.medisync.api.dto.request.AtencionUpdateRequest;
import com.medisync.api.dto.response.AtencionResponse;
import com.medisync.api.dto.response.EstadisticaAtencionResponse;
import com.medisync.api.service.AtencionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST encargado de gestionar las operaciones relacionadas
 * con las atenciones médicas.
 *
 * Expone los endpoints para crear, consultar, actualizar y eliminar
 * atenciones, así como para obtener consultas filtradas y estadísticas.
 *
 * El acceso a las operaciones está restringido mediante autorización
 * basada en roles utilizando Spring Security.
 */
@RestController
@RequestMapping("/api/atenciones")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Atenciones Médicas", description = "API para la gestión de atenciones médicas")
public class AtencionController {

    private final AtencionService atencionService;

    /**
     * Obtiene todas las atenciones médicas activas de forma paginada.
     *
     * El acceso a este endpoint está restringido a usuarios con rol ADMIN.
     *
     * @param pageable parámetros de paginación y ordenación.
     * @return página de atenciones médicas.
     */

    @Operation(summary = "Listar atenciones (Paginado)", description = "Obtiene todas las atenciones activas. Solo para ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AtencionResponse>> getAllAtenciones(Pageable pageable) {
        Page<AtencionResponse> atenciones = atencionService.getAllAtenciones(pageable);
        return ResponseEntity.ok(atenciones);
    }

    /**
     * Obtiene las atenciones médicas asociadas al paciente autenticado.
     *
     * El nombre de usuario se obtiene directamente del contexto de
     * autenticación de Spring Security.
     *
     * @param authentication información del usuario autenticado.
     * @return lista de atenciones correspondientes al paciente.
     */
    @Operation(summary = "Listar mis atenciones", description = "Obtiene las atenciones del paciente autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @GetMapping("/mias")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<List<AtencionResponse>> getMyAtenciones(Authentication authentication) {
        List<AtencionResponse> atenciones = atencionService.getMyAtenciones(authentication.getName());
        return ResponseEntity.ok(atenciones);
    }

    /**
     * Registra una nueva atención médica.
     *
     * Los datos recibidos son validados mediante Bean Validation antes
     * de ser enviados a la capa de servicio.
     *
     * @param request datos necesarios para crear la atención.
     * @return respuesta HTTP con la atención creada.
     */
    @Operation(summary = "Crear atención", description = "Crea una nueva atención médica. ADMIN o MEDICO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Atención creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Paciente o Empleado no encontrado")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<AtencionResponse> createAtencion(@Valid @RequestBody AtencionCreateRequest request) {
        AtencionResponse response = atencionService.createAtencion(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Actualiza una atención médica existente.
     *
     * @param id identificador de la atención que se desea actualizar.
     * @param atencionUpdateRequest datos utilizados para realizar
     * la actualización.
     * @return respuesta HTTP con la atención actualizada.
     */
    @Operation(summary = "Actualizar atención", description = "Actualiza una atención existente. ADMIN o MEDICO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atención actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Atención no encontrada")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<AtencionResponse> updateAtencion(@PathVariable Long id,
                                                           @Valid @RequestBody AtencionUpdateRequest atencionUpdateRequest) {
        AtencionResponse response = atencionService.updateAtencion(id, atencionUpdateRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Realiza el borrado lógico de una atención médica.
     *
     * La eliminación se delega en la capa de servicio, que establece
     * la atención como inactiva sin eliminar físicamente el registro.
     *
     * @param id identificador de la atención que se desea eliminar.
     * @return respuesta HTTP sin contenido.
     */
    @Operation(summary = "Eliminar atención", description = "Realiza un borrado lógico de la atención. Solo ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Atención eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Atención no encontrada")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAtencion(@PathVariable Long id) {
        atencionService.deleteAtencion(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene las atenciones médicas aplicando filtros opcionales
     * por empleado y rango de fechas.
     *
     * @param empleadoId identificador del empleado; puede ser {@code null}.
     * @param startDate fecha y hora inicial del rango; puede ser {@code null}.
     * @param endDate fecha y hora final del rango; puede ser {@code null}.
     * @return lista de atenciones que cumplen los filtros especificados.
     */
    @Operation(summary = "Consultar atenciones por filtros", description = "Consulta personalizada por empleado y rango de fechas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta exitosa")
    })
    @GetMapping("/filtros")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<List<AtencionResponse>> getAtencionesByFiltros(
            @RequestParam(required = false) Long empleadoId,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {
        List<AtencionResponse> response = atencionService.getAtencionesByFiltros(empleadoId, startDate, endDate);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene las estadísticas de las atenciones asociadas a un empleado.
     *
     * Las estadísticas incluyen el total de ingresos, el número de
     * atenciones y la distribución de las atenciones por estado.
     *
     * @param empleadoId identificador del empleado.
     * @return estadísticas correspondientes al empleado.
     */
    @Operation(summary = "Estadísticas por empleado", description = "Calcula totales y agrupaciones usando Java Streams para un empleado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estadísticas calculadas correctamente"),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    @GetMapping("/estadisticas/{empleadoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstadisticaAtencionResponse> getEstadisticasByEmpleado(@PathVariable Long empleadoId) {
        EstadisticaAtencionResponse response = atencionService.getEstadisticasByEmpleado(empleadoId);
        return ResponseEntity.ok(response);
    }
}