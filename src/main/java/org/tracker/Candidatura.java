package org.tracker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
/**
 * Representa una candidatura a una oferta de empleo.
 *
 * @param id               identificador único
 * @param titulo           puesto al que se postula
 * @param empresa          nombre de la empresa
 * @param estado           pendiente, entrevista, rechazado o aceptado
 * @param fechaPostulacion fecha en que se envió la candidatura
 * @param enlace           URL de la oferta (puede estar vacío)
 * @param notas            notas personales (puede estar vacío)
 */
public record Candidatura(int id, String titulo, String empresa, String estado,
                          LocalDate fechaPostulacion, String enlace, String notas) {

    public static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final List<String> ESTADOS = List.of("pendiente", "entrevista", "rechazado", "aceptado");
    /**
     * Comprueba que un campo no esté vacío.
     *
     * @param valor  el texto del campo
     * @param nombre nombre del campo (para el mensaje de error)
     * @return el texto sin espacios sobrantes
     * @throws IllegalArgumentException si está vacío
     */
    public static String obligatorio(String valor, String nombre) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("el campo " + nombre + " no puede estar vacío");
        }
        return valor.trim();
    }
    /**
     * Comprueba que el estado es válido y lo devuelve en minúsculas.
     *
     * @param estado texto del estado
     * @return el estado normalizado
     * @throws IllegalArgumentException si no es uno de los estados permitidos
     */
    public static String normalizarEstado(String estado) {
        String normalizado = estado.trim().toLowerCase();
        if (!ESTADOS.contains(normalizado)) {
            throw new IllegalArgumentException("estado '" + estado + "' no válido. Estados posibles: "
                    + String.join(", ", ESTADOS));
        }
        return normalizado;
    }
}
