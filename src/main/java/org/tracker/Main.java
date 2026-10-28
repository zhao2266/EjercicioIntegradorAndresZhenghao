package org.tracker;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * Punto de entrada de la aplicación. Recibe la operación y sus parámetros por argumentos,
 * la ejecuta y termina.
 */
public class Main {

    private static final Path FICHERO_DATOS = Path.of("datos.csv");

    public static void main(String[] args) {
        if (args.length == 0) {
            mostrarAyuda();
            System.exit(1);
        }
        RepositorioFichero repositorio = new RepositorioFichero(FICHERO_DATOS);
        try {
            ejecutar(args, repositorio);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error de fichero: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void ejecutar(String[] args, RepositorioFichero repositorio) throws IOException {
        switch (args[0]) {
            default -> throw new IllegalArgumentException("comando desconocido '" + args[0]
                    + "'. Ejecuta el programa sin argumentos para ver la ayuda");
        }
    }

    private static void mostrarAyuda() {
        System.out.println("Tracker de candidaturas. Uso:");
        System.out.println("  -listar [--estado X] [--empresa Y]");
        System.out.println("  -anadir --titulo X --empresa Y [--estado Z] [--fecha dd/MM/yyyy] [--enlace U] [--notas N]");
        System.out.println("  -ver <id>");
        System.out.println("  -borrar <id>");
        System.out.println("  -importar <fichero.csv>");
    }
    /**
     * Lee las opciones del tipo "--clave valor" que van después del comando.
     */
    private static Map<String, String> leerOpciones(String[] args, Set<String> permitidas) {
        Map<String, String> opciones = new HashMap<>();
        for (int i = 1; i < args.length; i += 2) {
            String clave = args[i];
            if (!permitidas.contains(clave)) {
                throw new IllegalArgumentException("opción no válida '" + clave + "'. Opciones de "
                        + args[0] + ": " + String.join(", ", permitidas));
            }
            if (i + 1 >= args.length) {
                throw new IllegalArgumentException("falta el valor de " + clave);
            }
            opciones.put(clave.substring(2), args[i + 1]);
        }
        return opciones;
    }

    private static String requerida(Map<String, String> opciones, String nombre) {
        if (!opciones.containsKey(nombre)) {
            throw new IllegalArgumentException("falta el argumento obligatorio --" + nombre);
        }
        return Candidatura.obligatorio(opciones.get(nombre), nombre);
    }

    private static int leerId(String[] args, String comando) {
        if (args.length != 2) {
            throw new IllegalArgumentException("uso: " + comando + " <id>");
        }
        try {
            return Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("el id '" + args[1] + "' no es un número");
        }
    }
    private static void listar(String[] args, RepositorioFichero repositorio) throws IOException {
        Map<String, String> opciones = leerOpciones(args, Set.of("--estado", "--empresa"));
        String estado = opciones.get("estado");
        if (estado != null) {
            estado = Candidatura.normalizarEstado(estado);
        }
        List<Candidatura> resultado = repositorio.filtrar(estado, opciones.get("empresa"));
        if (resultado.isEmpty()) {
            System.out.println("No hay candidaturas que mostrar.");
            return;
        }
        System.out.printf("%-4s %-30s %-14s %-12s %s%n", "ID", "TÍTULO", "EMPRESA", "ESTADO", "FECHA");
        for (Candidatura c : resultado) {
            System.out.printf("%-4d %-30s %-14s %-12s %s%n", c.id(), c.titulo(), c.empresa(), c.estado(),
                    c.fechaPostulacion().format(Candidatura.FORMATO_FECHA));
        }
        System.out.println(resultado.size() + " candidatura(s)");
    }
    private static void anadir(String[] args, RepositorioFichero repositorio) throws IOException {
        Map<String, String> opciones = leerOpciones(args,
                Set.of("--titulo", "--empresa", "--estado", "--fecha", "--enlace", "--notas"));
        String titulo = requerida(opciones, "titulo");
        String empresa = requerida(opciones, "empresa");
        String estado = Candidatura.normalizarEstado(opciones.getOrDefault("estado", "pendiente"));
        LocalDate fecha = opciones.containsKey("fecha")
                ? Candidatura.parsearFecha(opciones.get("fecha"))
                : LocalDate.now();
        Candidatura nueva = new Candidatura(0, titulo, empresa, estado, fecha,
                opciones.getOrDefault("enlace", ""), opciones.getOrDefault("notas", ""));
        Candidatura guardada = repositorio.anadir(nueva);
        System.out.println("Candidatura añadida con id " + guardada.id());
    }

    private static void ver(String[] args, RepositorioFichero repositorio) throws IOException {
        int id = leerId(args, "-ver");
        Candidatura c = repositorio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("no existe ninguna candidatura con id " + id));
        System.out.println("ID:         " + c.id());
        System.out.println("Título:     " + c.titulo());
        System.out.println("Empresa:    " + c.empresa());
        System.out.println("Estado:     " + c.estado());
        System.out.println("Fecha:      " + c.fechaPostulacion().format(Candidatura.FORMATO_FECHA));
        System.out.println("Enlace:     " + (c.enlace().isEmpty() ? "-" : c.enlace()));
        System.out.println("Notas:      " + (c.notas().isEmpty() ? "-" : c.notas()));
    }

    private static void borrar(String[] args, RepositorioFichero repositorio) throws IOException {
        int id = leerId(args, "-borrar");
        if (!repositorio.borrar(id)) {
            throw new IllegalArgumentException("no existe ninguna candidatura con id " + id);
        }
        System.out.println("Candidatura " + id + " borrada.");
    }

    private static void importar(String[] args, RepositorioFichero repositorio) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException("uso: -importar <fichero.csv>");
        }
        List<String> avisos = new ArrayList<>();
        int importadas = repositorio.importar(Path.of(args[1]), avisos);
        avisos.forEach(System.out::println);
        System.out.println(importadas + " candidatura(s) importadas.");
    }

}