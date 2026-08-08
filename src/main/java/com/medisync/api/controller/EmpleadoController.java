package com.medisync.api.controller;

import com.medisync.api.dto.response.EmpleadoResponse;
import com.medisync.api.service.EmpleadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST encargado de gestionar las operaciones relacionadas
 * con los empleados de la aplicación.
 *
 * Expone los endpoints necesarios para consultar empleados activos
 * y realizar su baja lógica.
 *
 * Los empleados pueden representar diferentes roles dentro del sistema,
 * como médicos o administradores.
 *
 * El acceso a los endpoints está protegido mediante Spring Security
 * y control de acceso basado en roles (RBAC).
 */
@RestController
@RequestMapping("/api/empleados")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Empleados", description = "API para gestión de empleados (Médicos/Admins)")
public class EmpleadoController {

    /**
     * Servicio encargado de gestionar la lógica de negocio
     * relacionada con los empleados.
     */
    private final EmpleadoService empleadoService;

    /**
     * Obtiene todos los empleados activos.
     *
     * Requiere que el usuario se encuentre autenticado.
     *
     * @return lista de empleados activos.
     */
    @Operation(
            summary = "Listar empleados",
            description = "Obtiene todos los empleados activos."
    )
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<EmpleadoResponse>> getAllEmpleados() {
        return ResponseEntity.ok(empleadoService.getAllEmpleados());
    }

    /**
     * Obtiene un empleado activo mediante su identificador.
     *
     * La respuesta incluye la información básica del empleado y
     * sus especialidades médicas asociadas.
     *
     * Requiere que el usuario se encuentre autenticado.
     *
     * @param id identificador del empleado.
     * @return información del empleado solicitado.
     */
    @Operation(
            summary = "Obtener empleado por ID",
            description = "Obtiene el detalle de un empleado, incluyendo sus especialidades."
    )
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EmpleadoResponse> getEmpleadoById(
            @PathVariable Long id) {

        return ResponseEntity.ok(empleadoService.getEmpleadoById(id));
    }

    /**
     * Realiza la baja lógica de un empleado.
     *
     * El empleado no se elimina físicamente de la base de datos,
     * sino que se marca como inactivo mediante el campo {@code activo}.
     *
     * Además, la lógica de negocio desactiva el usuario asociado,
     * impidiendo que pueda autenticarse posteriormente.
     *
     * Esta operación requiere el rol ADMIN.
     *
     * @param id identificador del empleado.
     * @return respuesta sin contenido con código HTTP 204.
     */
    @Operation(
            summary = "Eliminar empleado",
            description = "Da de baja lógica a un empleado. Solo ADMIN."
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEmpleado(@PathVariable Long id) {
        empleadoService.deleteEmpleado(id);
        return ResponseEntity.noContent().build();
    }
}