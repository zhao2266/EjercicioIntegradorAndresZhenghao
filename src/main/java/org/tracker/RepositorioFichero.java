package org.tracker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * Guarda y lee las candidaturas en un fichero de texto (una por línea, campos separados por ';').
 */
public class RepositorioFichero {
    private static final String CABECERA = "id;titulo;empresa;estado;fechaPostulacion;enlace;notas";

    private final Path ruta;

    /**
     * @param ruta ruta del fichero donde se guardan los datos
     */
    public RepositorioFichero(Path ruta) {
        this.ruta = ruta;
    }
    private void guardar(List<Candidatura> candidaturas) throws IOException {
        List<String> lineas = new ArrayList<>();
        lineas.add(CABECERA);
        for (Candidatura c : candidaturas) {
            lineas.add(c.aLinea());
        }
        Files.write(ruta, lineas, StandardCharsets.UTF_8);
    }
}