package com.medisync.api.controller;

import com.medisync.api.dto.request.PersonaRequest;
import com.medisync.api.dto.response.PersonaResponse;
import com.medisync.api.service.PersonaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST encargado de gestionar las operaciones relacionadas
 * con los datos básicos de las personas.
 *
 * Actualmente expone el endpoint necesario para registrar una nueva
 * persona en el sistema.
 */
@RestController
@RequestMapping("/api/personas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Personas", description = "API para gestión de datos de personas")
public class PersonaController {

    private final PersonaService personaService;

    /**
     * Registra una nueva persona en el sistema.
     *
     * Los datos recibidos se validan mediante Bean Validation antes
     * de ser enviados a la capa de servicio.
     *
     * El endpoint es público y no requiere autenticación.
     *
     * @param request DTO que contiene el nombre y email de la persona.
     * @return respuesta HTTP 201 con los datos de la persona creada.
     */
    @Operation(
            summary = "Crear persona",
            description = "Registra los datos base de una persona. Público."
    )
    @PostMapping
    public ResponseEntity<PersonaResponse> createPersona(
            @Valid @RequestBody PersonaRequest request) {

        PersonaResponse response = personaService.createPersona(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
