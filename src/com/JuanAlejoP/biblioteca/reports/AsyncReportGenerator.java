package com.JuanAlejoP.biblioteca.reports;

import java.util.List;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Generador de reportes en segundo plano utilizando un pool de hilos.
 * <p>
 * Esta clase delega la generación de diferentes tipos de reportes a un objeto
 * {@link GeneradorReportes} y ejecuta dichas tareas de forma asíncrona,
 * imprimiendo el progreso y los resultados por consola.
 * </p>
 */
public class AsyncReportGenerator {

    /**
     * Pool de hilos fijo con dos hilos para ejecutar tareas concurrentes.
     */
    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    /**
     * Encargado de generar los reportes solicitados.
     */
    private final GeneradorReportes generator;

    /**
     * Construye una instancia de AsyncReportGenerator con un generador de reportes.
     *
     * @param generator Objeto responsable de la creación de cada reporte.
     */
    public AsyncReportGenerator(GeneradorReportes generator) {
        this.generator = generator;
    }

    /**
     * Inicia la generación de una serie de reportes en segundo plano.
     * <p>
     * Se crea una lista de tareas {@code Callable<String>} que invocan distintos
     * métodos de {@code GeneradorReportes} y muestran el avance de forma secuencial
     * (20%, 40%, 60%, 80%, 100%).
     * </p>
     * <p>
     * Todas las tareas se envían al executor y se espera a su finalización con
     * Los resultados se imprimen en consola.
     * En caso de error, se captura la excepción y se muestra un mensaje de fallo.
     * </p>
     */
    public void generarReportesEnSegundoPlano() {
        List<Callable<String>> tasks = Arrays.asList(
                () -> { String r = generator.generarReporteRecursosPorEstado(); System.out.println("20% completado"); return r; },
                () -> { String r = generator.generarReporteRecursosConMasPrestamos(); System.out.println("40% completado"); return r; },
                () -> { String r = generator.generarReporteUsuariosActivos(); System.out.println("60% completado"); return r; },
                () -> { String r = generator.generarReporteRecursosConReservas(); System.out.println("80% completado"); return r; },
                () -> { String r = generator.generarEstadisticasPorCategoria(); System.out.println("100% completado"); return r; }
        );

        try {
            List<Future<String>> futures = executor.invokeAll(tasks);
            for (Future<String> future : futures) {
                // Imprime cada reporte una vez completado
                System.out.println(future.get());
            }
        } catch (Exception e) {
            // Muestra un mensaje genérico ante cualquier fallo en la generación
            System.out.println("Error en generación de reportes: " + e.getMessage());
        }
    }
}