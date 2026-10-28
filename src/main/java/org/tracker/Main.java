package org.tracker;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

    private static final Path FICHERO_DATOS = Path.of("datos.csv");

    public static void main(String[] args) {
        if (args.length == 0) {
            mostrarAyuda();
            System.exit(1);
        }
    }

    private static void mostrarAyuda() {
        System.out.println("Tracker de candidaturas. Uso:");
    }
}

private static void ejecutar(String[] args,
                             RepositorioFichero repositorio)
        throws IOException {

    switch (args[0]) {
        case "-listar" -> listar(args, repositorio);
        case "-anadir", "-añadir" -> anadir(args, repositorio);
        case "-ver" -> ver(args, repositorio);
        case "-borrar" -> borrar(args, repositorio);
        case "-importar" -> importar(args, repositorio);
        default -> throw new IllegalArgumentException(
                "comando desconocido");
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


