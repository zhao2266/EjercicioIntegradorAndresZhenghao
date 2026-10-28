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
}