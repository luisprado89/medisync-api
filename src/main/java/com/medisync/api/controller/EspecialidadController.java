package com.medisync.api.controller;

import com.medisync.api.dto.request.EspecialidadRequest;
import com.medisync.api.dto.response.EspecialidadResponse;
import com.medisync.api.service.EspecialidadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST encargado de gestionar los endpoints relacionados
 * con las especialidades médicas.
 *
 * Expone operaciones para crear, actualizar, eliminar y consultar
 * especialidades, aplicando control de acceso mediante roles y
 * autenticación.
 */
@RestController
@RequestMapping("/api/especialidades")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Especialidades", description = "API para la gestión de especialidades médicas")
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    /**
     * Crea una nueva especialidad médica.
     *
     * El acceso está restringido a usuarios con rol ADMIN o MEDICO.
     * Los datos recibidos son validados mediante Bean Validation.
     *
     * @param especialidadRequest DTO con los datos de la especialidad.
     * @return respuesta HTTP 201 con la especialidad creada.
     */
    @Operation(
            summary = "Crear especialidad",
            description = "Crea una nueva especialidad médica. ADMIN o MEDICO."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Especialidad creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o especialidad ya existe")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<EspecialidadResponse> createEspecialidad(
            @Valid @RequestBody EspecialidadRequest especialidadRequest) {

        EspecialidadResponse response =
                especialidadService.createEspecialidad(especialidadRequest);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Actualiza una especialidad existente.
     *
     * El acceso está restringido a usuarios con rol ADMIN o MEDICO.
     *
     * @param id identificador de la especialidad que se desea actualizar.
     * @param especialidadRequest DTO con los nuevos datos.
     * @return respuesta HTTP 200 con la especialidad actualizada.
     */
    @Operation(
            summary = "Actualizar especialidad",
            description = "Actualiza una especialidad existente. ADMIN o MEDICO."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Especialidad actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Especialidad no encontrada")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<EspecialidadResponse> updateEspecialidad(
            @PathVariable Long id,
            @Valid @RequestBody EspecialidadRequest especialidadRequest) {

        EspecialidadResponse response =
                especialidadService.updateEspecialidad(id, especialidadRequest);

        return ResponseEntity.ok(response);
    }

    /**
     * Realiza el borrado lógico de una especialidad.
     *
     * En lugar de eliminar físicamente el registro de la base de datos,
     * la especialidad se marca como inactiva.
     *
     * El acceso está restringido a usuarios con rol ADMIN o MEDICO.
     *
     * @param id identificador de la especialidad que se desea eliminar.
     * @return respuesta HTTP 204 cuando la operación se realiza correctamente.
     */
    @Operation(
            summary = "Eliminar especialidad",
            description = "Realiza un borrado lógico. ADMIN o MEDICO."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Especialidad eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Especialidad no encontrada")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<Void> deleteEspecialidad(@PathVariable Long id) {
        especialidadService.deleteEspecialidad(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene una especialidad mediante su identificador.
     *
     * Requiere que el usuario esté autenticado.
     *
     * @param id identificador de la especialidad.
     * @return respuesta HTTP 200 con los datos de la especialidad.
     */
    @Operation(
            summary = "Obtener especialidad por ID",
            description = "Obtiene una especialidad por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Especialidad encontrada"),
            @ApiResponse(responseCode = "404", description = "Especialidad no encontrada")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EspecialidadResponse> getEspecialidadById(
            @PathVariable Long id) {

        EspecialidadResponse response =
                especialidadService.getEspecialidadById(id);

        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene todas las especialidades activas.
     *
     * Requiere que el usuario esté autenticado.
     *
     * @return respuesta HTTP 200 con la lista de especialidades activas.
     */
    @Operation(
            summary = "Listar todas las especialidades",
            description = "Obtiene todas las especialidades activas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<EspecialidadResponse>> getAllEspecialidades() {
        List<EspecialidadResponse> response =
                especialidadService.getAllEspecialidades();

        return ResponseEntity.ok(response);
    }

    /**
     * Busca especialidades activas cuyo nombre contenga el texto indicado.
     *
     * Requiere que el usuario esté autenticado.
     *
     * @param nombre texto utilizado como criterio de búsqueda.
     * @return respuesta HTTP 200 con las especialidades encontradas.
     */
    @Operation(
            summary = "Buscar especialidades por nombre",
            description = "Busca especialidades que contengan el texto indicado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda exitosa")
    })
    @GetMapping("/buscar")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<EspecialidadResponse>> searchEspecialidadesByNombre(
            @RequestParam String nombre) {

        List<EspecialidadResponse> response =
                especialidadService.searchEspecialidadesByNombre(nombre);

        return ResponseEntity.ok(response);
    }
}
