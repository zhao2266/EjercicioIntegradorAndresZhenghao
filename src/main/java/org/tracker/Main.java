package org.tracker;

import java.nio.file.Path;

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

