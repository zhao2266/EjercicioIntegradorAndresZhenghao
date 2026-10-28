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

}