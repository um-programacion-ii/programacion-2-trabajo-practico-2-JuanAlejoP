import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collection;

public class GeneradorReportes {
    private GestorRecursos gestorRecursos;
    private GestorUsuarios gestorUsuarios;

    public GeneradorReportes(GestorRecursos gestorRecursos, GestorUsuarios gestorUsuarios) {
        this.gestorRecursos = gestorRecursos;
        this.gestorUsuarios = gestorUsuarios;
    }

    public String generarReporteRecursosPorEstado() {
        long disponibles = gestorRecursos.listAllResources().stream()
                .filter(r -> r.getEstado() == EstadoRecurso.DISPONIBLE)
                .count();
        long prestados = gestorRecursos.listAllResources().stream()
                .filter(r -> r.getEstado() == EstadoRecurso.PRESTADO)
                .count();
        long reservados = gestorRecursos.listAllResources().stream()
                .filter(r -> r.getEstado() == EstadoRecurso.RESERVADO)
                .count();

        return "Reporte de Recursos por Estado:\n" +
                "Disponibles: " + disponibles + "\n" +
                "Prestados: " + prestados + "\n" +
                "Reservados: " + reservados + "\n";
    }

    public String generarReporteRecursosConMasPrestamos() throws RecursoNoDisponibleException {
        Map<String, Integer> conteoPrestamos = new HashMap<>();

        for (Prestamo prestamo : gestorRecursos.getTodosLosPrestamos()) {
            String id = prestamo.getRecurso().getIdentificador();
            conteoPrestamos.put(id, conteoPrestamos.getOrDefault(id, 0) + 1);
        }

        List<Map.Entry<String, Integer>> top = conteoPrestamos.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(5)
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder("Top 5 Recursos más Prestados:\n");
        for (Map.Entry<String, Integer> entry : top) {
            RecursoDigital recurso = gestorRecursos.searchResourceById(entry.getKey());
            sb.append(recurso.getTitulo()).append(" - ").append(entry.getValue()).append(" veces\n");
        }
        return sb.toString();
    }

    public String generarReporteUsuariosActivos() {
        Map<String, Integer> prestamosPorUsuario = new HashMap<>();

        for (Prestamo prestamo : gestorRecursos.getTodosLosPrestamos()) {
            String idUsuario = prestamo.getUsuario().getId();
            prestamosPorUsuario.put(idUsuario, prestamosPorUsuario.getOrDefault(idUsuario, 0) + 1);
        }

        List<Map.Entry<String, Integer>> top = prestamosPorUsuario.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(5)
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder("Top 5 Usuarios más Activos:\n");
        for (Map.Entry<String, Integer> entry : top) {
            Usuario usuario = gestorUsuarios.searchUserById(entry.getKey());
            sb.append(usuario.getNombre()).append(" - ").append(entry.getValue()).append(" préstamos\n");
        }
        return sb.toString();
    }

    public String generarReporteRecursosConReservas() {
        List<RecursoDigital> recursosConReservas = gestorRecursos.listAllResources().stream()
                .filter(r -> !gestorRecursos.getReservas(r.getIdentificador()).isEmpty())
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder("Recursos con Reservas Activas:\n");
        for (RecursoDigital recurso : recursosConReservas) {
            int cantidadReservas = gestorRecursos.getReservas(recurso.getIdentificador()).size();
            sb.append(recurso.getTitulo()).append(" - ").append(cantidadReservas).append(" reservas\n");
        }
        return sb.toString();
    }

    public String generarEstadisticasPorCategoria() {
        Map<CategoriaRecurso, Long> conteoPorCategoria = gestorRecursos.listAllResources().stream()
                .collect(Collectors.groupingBy(RecursoDigital::getCategoria, Collectors.counting()));

        StringBuilder sb = new StringBuilder("Estadísticas por Categoría:\n");
        for (Map.Entry<CategoriaRecurso, Long> entry : conteoPorCategoria.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(" recursos\n");
        }
        return sb.toString();
    }
}