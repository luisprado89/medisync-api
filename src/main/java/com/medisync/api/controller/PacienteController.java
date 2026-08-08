package com.medisync.api.controller;

import com.medisync.api.dto.response.PacienteResponse;
import com.medisync.api.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST encargado de gestionar las operaciones relacionadas
 * con los pacientes.
 *
 * Expone los endpoints necesarios para consultar pacientes activos
 * y realizar su baja lógica.
 *
 * El acceso a los endpoints está protegido mediante Spring Security
 * y control de acceso basado en roles (RBAC).
 */
@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Pacientes", description = "API para gestión de pacientes")
public class PacienteController {

    /**
     * Servicio encargado de gestionar la lógica de negocio
     * relacionada con los pacientes.
     */
    private final PacienteService pacienteService;

    /**
     * Obtiene todos los pacientes activos.
     *
     * Requiere que el usuario se encuentre autenticado.
     *
     * @return lista de pacientes activos.
     */
    @Operation(
            summary = "Listar pacientes",
            description = "Obtiene todos los pacientes activos."
    )
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PacienteResponse>> getAllPacientes() {
        return ResponseEntity.ok(pacienteService.getAllPacientes());
    }

    /**
     * Obtiene un paciente activo mediante su identificador.
     *
     * Requiere que el usuario se encuentre autenticado.
     *
     * @param id identificador del paciente.
     * @return información del paciente solicitado.
     */
    @Operation(
            summary = "Obtener paciente por ID",
            description = "Obtiene el detalle de un paciente."
    )
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PacienteResponse> getPacienteById(
            @PathVariable Long id) {

        return ResponseEntity.ok(pacienteService.getPacienteById(id));
    }

    /**
     * Realiza la baja lógica de un paciente.
     *
     * El paciente no se elimina físicamente de la base de datos,
     * sino que se marca como inactivo mediante el campo {@code activo}.
     *
     * Además, la lógica de negocio desactiva el usuario asociado,
     * impidiendo que pueda autenticarse posteriormente.
     *
     * Requiere el rol ADMIN o MEDICO.
     *
     * @param id identificador del paciente.
     * @return respuesta sin contenido con código HTTP 204.
     */
    @Operation(
            summary = "Eliminar paciente",
            description = "Da de baja lógica a un paciente. ADMIN o MEDICO."
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<Void> deletePaciente(@PathVariable Long id) {
        pacienteService.deletePaciente(id);
        return ResponseEntity.noContent().build();
    }
}