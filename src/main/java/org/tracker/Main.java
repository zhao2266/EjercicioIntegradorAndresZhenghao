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


