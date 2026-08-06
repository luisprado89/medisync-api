package com.medisync.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones de la aplicación.
 *
 * <p>Centraliza el tratamiento de las excepciones lanzadas por los controladores REST, devolviendo respuestas HTTP
 * uniformes y con un formato común para todos los errores.</p>
 *
 * <p>Mediante la anotación {@code @RestControllerAdvice}, Spring Boot detecta automáticamente esta clase e intercepta
 * las excepciones que no hayan sido gestionadas por los controladores.</p>
 */


@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Gestiona las excepciones producidas cuando el recurso solicitado
     * no existe.
     *
     * @param ex excepción lanzada con la información del recurso no encontrado.
     * @return respuesta HTTP 404 con el detalle del error.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(), // Esto devuelve el número 404
                "Recurso no encontrado",
                ex.getMessage() // Extrae el mensaje del throw new...
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Gestiona las excepciones relacionadas con reglas de negocio.
     *
     * @param ex excepción lanzada cuando una operación incumple una regla definida por la aplicación.
     * @return respuesta HTTP 400 con la descripción del problema.
     */
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRule(BusinessRuleException ex) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Error de regla de negocio",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    /**
     * Gestiona los errores de validación generados por {@code @Valid}.
     *
     * <p>Recopila todos los campos que no cumplen las restricciones de
     * validación y devuelve una respuesta con el detalle de cada uno.</p>
     *
     * @param ex excepción generada por Spring durante la validación.
     * @return respuesta HTTP 400 con los errores de validación.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {

        // Creamos un mapa (diccionario) vacío para guardar los errores.
        // Ejemplo de lo que queremos construir: {"nombre": "es obligatorio", "precio": "debe ser mayor a 0"}
        Map<String, String> errors = new HashMap<>();

        // USO DE LAMBDAS AQUÍ! (Cumpliendo un requisito del enunciado)
        // ex.getBindingResult().getAllErrors() nos da una lista de todos los campos que fallaron.
        // .forEach() significa: "Para cada error que encontraste, haz lo siguiente:"
        ex.getBindingResult().getAllErrors().forEach(error -> {

            // Convertimos el error genérico a un FieldError para poder sacarle el nombre del campo.
            String fieldName = ((FieldError) error).getField(); // Ej: "nombre"

            // Sacamos el mensaje que pusimos en el DTO (ej: "El nombre es obligatorio").
            String errorMessage = error.getDefaultMessage();

            // Lo guardamos en el diccionario.
            errors.put(fieldName, errorMessage);
        });

        // Devolvemos el diccionario convertido a String dentro de nuestro ErrorResponse.
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(), // 400
                "Error de validación",
                errors.toString() // Se verá como: {nombre=El nombre es obligatorio}
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Gestiona cualquier excepción no controlada de forma específica.
     *
     * <p>Actúa como mecanismo de seguridad para evitar que errores inesperados
     * lleguen directamente al cliente.</p>
     *
     * @param ex excepción producida durante la ejecución.
     * @return respuesta HTTP 500 con la información del error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(), // Esto devuelve el número 500
                "Error interno del servidor",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    /**
     * Representa el formato estándar de las respuestas de error devueltas por la API.
     *
     * @param timestamp fecha y hora en la que se produjo el error.
     * @param status código de estado HTTP.
     * @param error descripción breve del tipo de error.
     * @param message detalle del error.
     */
    public record ErrorResponse(
            LocalDateTime timestamp, // La fecha y hora del error
            int status,              // El código HTTP (400, 404, 500)
            String error,            // El título del error
            String message           // El detalle del error
    ) {}
}
