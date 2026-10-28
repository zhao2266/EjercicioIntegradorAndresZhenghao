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
    /**
     * Lee todas las candidaturas del fichero. Si el fichero no existe devuelve una lista vacía.
     *
     * @return la lista de candidaturas
     * @throws IOException              si falla la lectura
     * @throws IllegalArgumentException si alguna línea del fichero está mal
     */
    public List<Candidatura> leerTodas() throws IOException {
        List<Candidatura> candidaturas = new ArrayList<>();
        if (!Files.exists(ruta)) {
            return candidaturas;
        }
        List<String> lineas = Files.readAllLines(ruta, StandardCharsets.UTF_8);
        for (int i = 0; i < lineas.size(); i++) {
            String linea = lineas.get(i);
            if (linea.isBlank() || linea.startsWith("id;")) {
                continue;
            }
            try {
                candidaturas.add(Candidatura.desdeLinea(linea));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("línea " + (i + 1) + " de " + ruta + ": " + e.getMessage());
            }
        }
        return candidaturas;
    }
}