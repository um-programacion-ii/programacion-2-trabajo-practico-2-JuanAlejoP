import java.util.concurrent.*;
import java.util.List;
import java.util.Arrays;

public class AsyncReportGenerator {
    private ExecutorService executor = Executors.newFixedThreadPool(2);
    private GeneradorReportes generator;

    public AsyncReportGenerator(GeneradorReportes generator) {
        this.generator = generator;
    }

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
                System.out.println(future.get());
            }
        } catch (Exception e) {
            System.out.println("Error en generación de reportes: " + e.getMessage());
        }
    }
}