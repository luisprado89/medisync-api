package com.medisync.api.mapper;

import com.medisync.api.dto.request.AtencionCreateRequest;
import com.medisync.api.dto.request.AtencionUpdateRequest;
import com.medisync.api.dto.response.AtencionResponse;
import com.medisync.api.entity.Atencion;
import com.medisync.api.entity.Empleado;
import com.medisync.api.entity.Paciente;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * Componente encargado de realizar la conversión entre la entidad
 * {@link Atencion} y los distintos DTOs utilizados por la API.
 *
 * Centraliza la lógica de transformación entre la capa de dominio y la
 * capa de presentación, evitando duplicar este código en los servicios
 * y controladores.
 */
@Component
public class AtencionMapper {
    /**
     * Convierte un DTO de creación en una nueva entidad {@link Atencion}.
     *
     * Asocia la atención con el paciente y el empleado recibidos como
     * parámetros, construyendo una entidad lista para ser persistida.
     *
     * @param request datos recibidos para registrar la atención.
     * @param paciente paciente asociado a la atención.
     * @param empleado empleado responsable de la atención.
     * @return entidad {@link Atencion} construida a partir del DTO.
     */
    public Atencion toEntity(AtencionCreateRequest request, Paciente paciente, Empleado empleado) {
        return Atencion.builder()
                .fecha(request.getFecha())
                .motivo(request.getMotivo())
                .importe(request.getImporte())
                .estado(request.getEstado())
                .paciente(paciente)
                .empleado(empleado)
                .build();
    }
    /**
     * Actualiza una entidad {@link Atencion} con los datos proporcionados
     * en el DTO de actualización.
     *
     * Solo se modifican aquellos atributos cuyo valor haya sido enviado en
     * la solicitud, manteniendo el resto de la información sin cambios.
     *
     * @param request datos utilizados para actualizar la atención.
     * @param atencion entidad que será actualizada.
     */
    public void updateEntityFromRequest(AtencionUpdateRequest request, Atencion atencion) {
        if (request.getFecha() != null) atencion.setFecha(request.getFecha());
        if (request.getMotivo() != null) atencion.setMotivo(request.getMotivo());
        if (request.getImporte() != null) atencion.setImporte(request.getImporte());
        if (request.getEstado() != null) atencion.setEstado(request.getEstado());
    }
    /**
     * Convierte una entidad {@link Atencion} en un DTO de respuesta.
     *
     * Además de la información propia de la atención, incorpora los nombres
     * del paciente y del empleado responsable para facilitar su consumo por
     * parte del cliente.
     *
     * @param atencion entidad que se desea transformar.
     * @return DTO con la información de la atención.
     */
    public AtencionResponse toResponse(Atencion atencion) {
        return AtencionResponse.builder()
                .id(atencion.getId())
                .fecha(atencion.getFecha())
                .motivo(atencion.getMotivo())
                .importe(atencion.getImporte())
                .estado(atencion.getEstado())
                .pacienteNombre(atencion.getPaciente().getPersona().getNombre())
                .empleadoNombre(atencion.getEmpleado().getPersona().getNombre())
                .build();
    }
    /**
     * Convierte una colección de entidades {@link Atencion} en una lista de
     * DTOs de respuesta.
     *
     * @param atenciones lista de entidades a transformar.
     * @return lista de objetos {@link AtencionResponse}.
     */
    public List<AtencionResponse> toResponseList(List<Atencion> atenciones) {
        return atenciones.stream()
                .map(this::toResponse)
                .toList(); //En Java 17 o incluso Java 21, ya no tenemos que hacer '.collect(Collectors.toList())' solo '.toList()'.
    }
}