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
    /**
     * Convierte un texto dd/MM/yyyy en una fecha.
     *
     * @param texto la fecha escrita
     * @return la fecha
     * @throws IllegalArgumentException si el formato no es dd/MM/yyyy
     */
    public static LocalDate parsearFecha(String texto) {
        try {
            return LocalDate.parse(texto.trim(), FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("la fecha '" + texto + "' no tiene el formato dd/MM/yyyy");
        }
    }
    /**
     * Convierte la candidatura en una línea del fichero, con campos separados por ';'.
     *
     * @return la línea lista para guardar
     */
    public String aLinea() {
        return String.join(";",
                String.valueOf(id),
                limpiar(titulo),
                limpiar(empresa),
                estado,
                fechaPostulacion.format(FORMATO_FECHA),
                limpiar(enlace),
                limpiar(notas));
    }

    private static String limpiar(String texto) {
        return texto == null ? "" : texto.replace(";", ",").replace("\n", " ").trim();
    }
    /**
     * Crea una candidatura a partir de una línea del fichero.
     *
     * @param linea línea con los campos separados por ';'
     * @return la candidatura leída
     * @throws IllegalArgumentException si la línea no tiene un formato válido
     */
    public static Candidatura desdeLinea(String linea) {
        String[] campos = linea.split(";", -1);
        if (campos.length < 5) {
            throw new IllegalArgumentException("faltan campos (se esperan al menos 5, hay " + campos.length + ")");
        }
        int id;
        try {
            id = Integer.parseInt(campos[0].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("el id '" + campos[0] + "' no es un número");
        }
        String titulo = obligatorio(campos[1], "titulo");
        String empresa = obligatorio(campos[2], "empresa");
        String estado = normalizarEstado(campos[3]);
        LocalDate fecha = parsearFecha(campos[4]);
        String enlace = campos.length > 5 ? campos[5].trim() : "";
        String notas = campos.length > 6 ? campos[6].trim() : "";
        return new Candidatura(id, titulo, empresa, estado, fecha, enlace, notas);
    }
    /**
     * Devuelve una copia de la candidatura con otro id.
     *
     * @param nuevoId el id nuevo
     * @return la copia
     */
    public Candidatura conId(int nuevoId) {
        return new Candidatura(nuevoId, titulo, empresa, estado, fechaPostulacion, enlace, notas);
    }
}
