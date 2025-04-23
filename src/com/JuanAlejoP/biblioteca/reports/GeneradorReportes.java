package com.JuanAlejoP.biblioteca.reports;

import com.JuanAlejoP.biblioteca.manager.GestorRecursos;
import com.JuanAlejoP.biblioteca.manager.GestorUsuarios;
import com.JuanAlejoP.biblioteca.model.CategoriaRecurso;
import com.JuanAlejoP.biblioteca.model.EstadoRecurso;
import com.JuanAlejoP.biblioteca.model.Prestamo;
import com.JuanAlejoP.biblioteca.model.RecursoDigital;
import com.JuanAlejoP.biblioteca.model.Usuario;
import com.JuanAlejoP.biblioteca.model.RecursoNoDisponibleException;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generador de distintos reportes basados en los recursos y usuarios de la biblioteca.
 * <p>
 * Esta clase agrupa lógica para compilar estadísticas y listados:
 * <ul>
 *     <li>Recursos por estado (disponibles, prestados, reservados).</li>
 *     <li>Top de recursos más prestados.</li>
 *     <li>Top de usuarios más activos.</li>
 *     <li>Recursos con reservas activas.</li>
 *     <li>Estadísticas por categoría de recurso.</li>
 * </ul>
 * </p>
 */
public class GeneradorReportes {

    /**
     * Gestor encargado de las operaciones sobre recursos.
     */
    private final GestorRecursos gestorRecursos;

    /**
     * Gestor encargado de las operaciones sobre usuarios.
     */
    private final GestorUsuarios gestorUsuarios;

    /**
     * Construye un GeneradorReportes con los gestores de recursos y usuarios.
     *
     * @param gestorRecursos Gestor para acceder y consultar recursos.
     * @param gestorUsuarios Gestor para acceder y consultar usuarios.
     */
    public GeneradorReportes(GestorRecursos gestorRecursos, GestorUsuarios gestorUsuarios) {
        this.gestorRecursos = gestorRecursos;
        this.gestorUsuarios = gestorUsuarios;
    }

    /**
     * Genera un reporte con el conteo de recursos por su estado.
     *
     * @return Texto con las cantidades de recursos disponibles, prestados y reservados.
     */
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

    /**
     * Genera el listado de los 5 recursos más prestados.
     *
     * @return Texto con título y las veces que cada recurso fue prestado.
     * @throws RecursoNoDisponibleException Si falla la búsqueda de un recurso por ID.
     */
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
            sb.append(recurso.getTitulo())
                    .append(" - ")
                    .append(entry.getValue())
                    .append(" veces\n");
        }
        return sb.toString();
    }

    /**
     * Genera el listado de los 5 usuarios con más préstamos.
     *
     * @return Texto con nombre de usuario y cantidad de préstamos realizados.
     */
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
            sb.append(usuario.getNombre())
                    .append(" - ")
                    .append(entry.getValue())
                    .append(" préstamos\n");
        }
        return sb.toString();
    }

    /**
     * Genera una lista de todos los recursos que actualmente tienen reservas activas.
     *
     * @return Texto con título y cada recurso junto a su número de reservas.
     */
    public String generarReporteRecursosConReservas() {
        List<RecursoDigital> recursosConReservas = gestorRecursos.listAllResources().stream()
                .filter(r -> !gestorRecursos.getReservas(r.getIdentificador()).isEmpty())
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder("Recursos con Reservas Activas:\n");
        for (RecursoDigital recurso : recursosConReservas) {
            int cantidadReservas = gestorRecursos.getReservas(recurso.getIdentificador()).size();
            sb.append(recurso.getTitulo())
                    .append(" - ")
                    .append(cantidadReservas)
                    .append(" reservas\n");
        }
        return sb.toString();
    }

    /**
     * Genera estadísticas de la cantidad de recursos por categoría.
     *
     * @return Texto con cada categoría y el número de recursos asociados.
     */
    public String generarEstadisticasPorCategoria() {
        Map<CategoriaRecurso, Long> conteoPorCategoria = gestorRecursos.listAllResources().stream()
                .collect(Collectors.groupingBy(RecursoDigital::getCategoria, Collectors.counting()));

        StringBuilder sb = new StringBuilder("Estadísticas por Categoría:\n");
        for (Map.Entry<CategoriaRecurso, Long> entry : conteoPorCategoria.entrySet()) {
            sb.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append(" recursos\n");
        }
        return sb.toString();
    }
}