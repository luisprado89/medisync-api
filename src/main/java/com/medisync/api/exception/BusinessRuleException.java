package com.medisync.api.exception;

/**
 * Excepción personalizada para representar errores de reglas de negocio.
 *
 * <p>Extiende de {@link RuntimeException} porque este tipo de errores no son fallos técnicos del sistema, sino
 * situaciones en las que los datos recibidos incumplen una regla de negocio (ej: un precio negativo o un stock
 * insuficiente).</p>
 *
 * <p>Al ser una excepción no comprobada (Unchecked Exception), no es necesario declararla con {@code throws}
 * ni capturarla con {@code try-catch} en cada llamada. Spring Boot puede interceptarla mediante un manejador global
 * de excepciones (ej: {@code @RestControllerAdvice}) y devolver una respuesta HTTP adecueda al cliente.</p>
 */
public class BusinessRuleException extends RuntimeException {
    /**
     * Crea una nueva excepción con el mensaje que describe la regla de negocio que ha sido incumplida.
     * @param message descripción del motivo por el que no puede realizarse la operación.
     */
    public  BusinessRuleException(String message) {
        super(message);
    }
}
