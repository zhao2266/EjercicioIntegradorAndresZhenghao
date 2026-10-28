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
    /**
     * Añade una candidatura nueva con el siguiente id libre.
     *
     * @param nueva candidatura (su id se ignora)
     * @return la candidatura guardada, con su id
     * @throws IOException si falla la lectura o escritura
     */
    public Candidatura anadir(Candidatura nueva) throws IOException {
        List<Candidatura> todas = leerTodas();
        Candidatura guardada = nueva.conId(siguienteId(todas));
        todas.add(guardada);
        guardar(todas);
        return guardada;
    }

    private int siguienteId(List<Candidatura> candidaturas) {
        return candidaturas.stream().mapToInt(Candidatura::id).max().orElse(0) + 1;
    }
    /**
     * Busca una candidatura por su id.
     *
     * @param id el id a buscar
     * @return la candidatura, o vacío si no existe
     * @throws IOException si falla la lectura
     */
    public Optional<Candidatura> buscarPorId(int id) throws IOException {
        return leerTodas().stream().filter(c -> c.id() == id).findFirst();
    }
    /**
     * Borra una candidatura.
     *
     * @param id el id de la candidatura
     * @return true si existía y se borró, false si no había ninguna con ese id
     * @throws IOException si falla la lectura o escritura
     */
    public boolean borrar(int id) throws IOException {
        List<Candidatura> todas = leerTodas();
        boolean borrada = todas.removeIf(c -> c.id() == id);
        if (borrada) {
            guardar(todas);
        }
        return borrada;
    }
    /**
     * Filtra las candidaturas por estado y/o empresa. Un filtro null no se aplica.
     *
     * @param estado  estado exacto (sin importar mayúsculas), o null
     * @param empresa texto que debe contener el nombre de la empresa, o null
     * @return las candidaturas que cumplen los filtros
     * @throws IOException si falla la lectura
     */
    public List<Candidatura> filtrar(String estado, String empresa) throws IOException {
        List<Candidatura> resultado = new ArrayList<>();
        for (Candidatura c : leerTodas()) {
            boolean estadoOk = estado == null || c.estado().equalsIgnoreCase(estado.trim());
            boolean empresaOk = empresa == null || c.empresa().toLowerCase().contains(empresa.trim().toLowerCase());
            if (estadoOk && empresaOk) {
                resultado.add(c);
            }
        }
        return resultado;
    }
}