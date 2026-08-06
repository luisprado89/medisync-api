package com.medisync.api.exception;
/**
 * Excepción personalizada que indica que un recurso solicitado no existe o no ha podido ser encontrado.
 *
 * <p>Extiende de {@link RuntimeException} porque la ausencia de un recurso (ej: un producto, una venta o una sucursal)
 * es una situación que puede producirse durante la ejecución de la aplicación y no requiere ser gestionada con
 * {@code try-catch} en cada llamada.</p>
 *
 * <p>Spring Boot puede capturar esta excepción mediante un manejador global de excepciones (ej: {@code @RestControllerAdvice})
 * y devolver una respuesta HTTP apropiada, como un código {@code 404 Not Found}.</p>
 */
public class ResourceNotFoundException extends  RuntimeException {
    /**
     * Crea una nueva excepción con el mensaje que describe el recurso que no ha podido ser encontrado.
     * @param message descripción del recurso inexistente o no encontrado.
     */
    public  ResourceNotFoundException(String message) {
        super(message);
    }
}
