# Tracker de Candidaturas Laborales

Aplicación en Java para llevar el seguimiento de las candidaturas de empleo o prácticas: a qué empresas hemos enviado el CV, en qué estado está cada una, etc.

Proyecto del ejercicio integrador del primer trimestre (Acceso a Datos y Desarrollo de Interfaces).

## Equipo

- Zhenghao
- Andres

## Tecnologías

- Java 24
- Maven
- Archivos de texto (CSV con `;`) para guardar los datos
- Figma (prototipo y manual de estilo)

## Cómo compilar y ejecutar (v1.0)

Compilar:

```
mvn clean package
```

Ejecutar (desde la carpeta del proyecto):

```
java -cp target/classes tracker.Main -listar
java -cp target/classes tracker.Main -listar --estado pendiente --empresa Indra
java -cp target/classes tracker.Main -anadir --titulo "Dev Java" --empresa Indra --estado pendiente --fecha 15/09/2026
java -cp target/classes tracker.Main -ver 13
java -cp target/classes tracker.Main -borrar 13
java -cp target/classes tracker.Main -importar candidaturas.csv
```

## Datos de prueba

El archivo con los datos de ejemplo es `candidaturas.csv`, en la raíz del proyecto.

## Diseño

- Prototipo en Figma (solo lectura): https://www.figma.com/proto/HEcHbBfe3gu61ILh8ACysF/FigmaZhenghaoAndres?node-id=0-1&p=f&t=VsbpKpXu2Uy9ioRh-0&scaling=min-zoom&content-scaling=fixed&page-id=0%3A1&starting-point-node-id=6%3A9820

- Manual de estilo y pantallas: : carpeta `diseno/`

