package com.medisync.api.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * DTO utilizado para devolver información estadística sobre las
 * atenciones médicas de un empleado.
 *
 * Incluye indicadores agregados, como el número total de atenciones,
 * los ingresos generados y la distribución de las atenciones según
 * su estado.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EstadisticaAtencionResponse {
    /**
     * Nombre del empleado al que pertenecen las estadísticas.
     */
    private String empleadoNombre;
    /**
     * Importe total generado por las atenciones.
     */
    private BigDecimal totalIngresos;
    /**
     * Número total de atenciones registradas.
     */
    private Long cantidadAtenciones;
    /**
     * Distribución de las atenciones agrupadas por estado.
     *
     * La clave representa el nombre del estado y el valor el número de
     * atenciones registradas en dicho estado.
     */
    private Map<String, Long> atencionesPorEstado;
}