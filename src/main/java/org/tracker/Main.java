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
