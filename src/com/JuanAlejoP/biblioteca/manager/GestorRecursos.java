package com.JuanAlejoP.biblioteca.manager;

import com.JuanAlejoP.biblioteca.model.*;
import com.JuanAlejoP.biblioteca.Prestable;
import com.JuanAlejoP.biblioteca.Renovable;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.Comparator;

/**
 * GestorRecursos se encarga de administrar los recursos digitales de la biblioteca,
 * incluyendo operaciones de búsqueda, listado, préstamo, devolución, reserva y renovación.
 */
public class GestorRecursos {
    /**
     * Mapa concurrente de recursos digitales, indexado por su identificador único.
     */
    private ConcurrentMap<String, RecursoDigital> recursos;

    /**
     * Mapa concurrente de préstamos activos, donde la clave es el ID del recurso prestado.
     */
    private ConcurrentMap<String, Prestamo> prestamos;

    /**
     * Mapa concurrente de colas de reservas para cada recurso, indexado por ID de recurso.
     */
    private ConcurrentMap<String, Queue<Reserva>> reservas;

    /**
     * Inicializa el gestor con estructuras de datos concurrentes vacías para recursos,
     * préstamos y reservas.
     */
    public GestorRecursos() {
        this.recursos = new ConcurrentHashMap<>();
        this.prestamos = new ConcurrentHashMap<>();
        this.reservas = new ConcurrentHashMap<>();
    }

    /**
     * Agrega un nuevo recurso digital al inventario.
     *
     * @param recursoDigital instancia de {@link RecursoDigital} a agregar
     */
    public void addNewResource(RecursoDigital recursoDigital) {
        recursos.put(recursoDigital.getIdentificador(), recursoDigital);
    }

    /**
     * Busca un recurso digital por su identificador.
     *
     * @param id identificador del recurso a buscar
     * @return instancia de {@link RecursoDigital} con el ID proporcionado
     * @throws RecursoNoDisponibleException si no existe un recurso con ese ID
     */
    public RecursoDigital searchResourceById(String id) throws RecursoNoDisponibleException {
        RecursoDigital rd = recursos.get(id);
        if (rd == null) {
            throw new RecursoNoDisponibleException("Recurso con ID " + id + " no encontrado.");
        }
        return rd;
    }

    /**
     * Devuelve todos los recursos digitales registrados.
     *
     * @return colección de todos los {@link RecursoDigital}
     */
    public Collection<RecursoDigital> listAllResources() {
        return recursos.values();
    }

    /**
     * Busca recursos cuyo título contenga la cadena especificada, sin distinción de mayúsculas.
     *
     * @param title parte o totalidad del título a buscar
     * @return lista de recursos que coinciden con el criterio de búsqueda
     */
    public List<RecursoDigital> searchByTitle(String title) {
        return recursos.values().stream()
                .filter(r -> r.getTitulo().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Filtra recursos de tipo {@link RecursoBase} por categoría.
     *
     * @param categoria categoría de recurso a filtrar
     * @return lista de recursos que pertenecen a la categoría dada
     */
    public List<RecursoDigital> filterByCategoria(CategoriaRecurso categoria) {
        return recursos.values().stream()
                .filter(r -> r instanceof RecursoBase && ((RecursoBase) r).getCategoria() == categoria)
                .collect(Collectors.toList());
    }

    /**
     * Ordena los recursos por título en orden alfabético.
     *
     * @return lista de recursos ordenados por título
     */
    public List<RecursoDigital> sortByTitulo() {
        return recursos.values().stream()
                .sorted(Comparator.comparing(RecursoDigital::getTitulo))
                .collect(Collectors.toList());
    }

    /**
     * Ordena los recursos de tipo {@link RecursoBase} por nombre de categoría.
     *
     * @return lista de recursos ordenados por categoría
     */
    public List<RecursoDigital> sortByCategoria() {
        return recursos.values().stream()
                .filter(r -> r instanceof RecursoBase)
                .sorted(Comparator.comparing(r -> ((RecursoBase) r).getCategoria().toString()))
                .collect(Collectors.toList());
    }

    // ------ Préstamos y Reservas ------

    /**
     * Presta un recurso a un usuario, cambiando su estado y registrando el préstamo.
     *
     * @param idRecurso ID del recurso a prestar
     * @param usuario   usuario que recibe el préstamo
     * @throws RecursoNoDisponibleException si el recurso no existe, no puede prestarse o no está disponible
     */
    public void prestarRecurso(String idRecurso, Usuario usuario) throws RecursoNoDisponibleException {
        RecursoDigital recurso = searchResourceById(idRecurso);
        if (!(recurso instanceof Prestable)) {
            throw new RecursoNoDisponibleException("Este recurso no puede ser prestado.");
        }
        if (recurso.getEstado() != EstadoRecurso.DISPONIBLE) {
            throw new RecursoNoDisponibleException("El recurso no está disponible.");
        }
        recurso.actualizarEstado(EstadoRecurso.PRESTADO);
        prestamos.put(idRecurso, new Prestamo(recurso, usuario));
    }

    /**
     * Devuelve un recurso y actualiza su estado según si hay reservas pendientes.
     *
     * @param idRecurso ID del recurso a devolver
     * @throws RecursoNoDisponibleException si el recurso no está prestado o no existe
     */
    public void devolverRecurso(String idRecurso) throws RecursoNoDisponibleException {
        RecursoDigital recurso = searchResourceById(idRecurso);
        if (!prestamos.containsKey(idRecurso)) {
            throw new RecursoNoDisponibleException("Recurso no está prestado.");
        }
        prestamos.remove(idRecurso);
        Queue<Reserva> cola = reservas.get(idRecurso);
        if (cola != null && !cola.isEmpty()) {
            recurso.actualizarEstado(EstadoRecurso.RESERVADO);
        } else {
            recurso.actualizarEstado(EstadoRecurso.DISPONIBLE);
        }
    }

    /**
     * Añade una reserva para un recurso que no está disponible.
     *
     * @param idRecurso ID del recurso a reservar
     * @param usuario   usuario que realiza la reserva
     * @throws RecursoNoDisponibleException si el recurso no existe, no puede reservarse o está disponible
     */
    public void reservarRecurso(String idRecurso, Usuario usuario) throws RecursoNoDisponibleException {
        RecursoDigital recurso = searchResourceById(idRecurso);
        if (!(recurso instanceof Prestable)) {
            throw new RecursoNoDisponibleException("Este recurso no puede reservarse.");
        }
        if (recurso.getEstado() == EstadoRecurso.DISPONIBLE) {
            throw new RecursoNoDisponibleException("Recurso disponible, reserva no necesaria.");
        }
        reservas.computeIfAbsent(idRecurso, k -> new ConcurrentLinkedQueue<>())
                .add(new Reserva(recurso, usuario));
    }

    /**
     * Renueva el préstamo de un recurso si es renovable y pertenece al usuario.
     *
     * @param idRecurso ID del recurso a renovar
     * @param usuario   usuario que solicita la renovación
     * @throws RecursoNoDisponibleException si el recurso no permite renovación o no corresponde al préstamo
     */
    public void renovarRecurso(String idRecurso, Usuario usuario) throws RecursoNoDisponibleException {
        RecursoDigital recurso = searchResourceById(idRecurso);
        if (!(recurso instanceof Renovable)) {
            throw new RecursoNoDisponibleException("Este recurso no permite renovación.");
        }
        Prestamo prestamo = prestamos.get(idRecurso);
        if (prestamo == null || !prestamo.getUsuario().getId().equals(usuario.getId())) {
            throw new RecursoNoDisponibleException("No es su préstamo.");
        }
        prestamos.put(idRecurso, new Prestamo(recurso, usuario));
    }

    /**
     * Obtiene el préstamo activo de un recurso.
     *
     * @param idRecurso ID del recurso
     * @return instancia de {@link Prestamo} o null si no existe
     */
    public Prestamo getPrestamo(String idRecurso) {
        return prestamos.get(idRecurso);
    }

    /**
     * Recupera todas las reservas asociadas a un recurso.
     *
     * @param idRecurso ID del recurso
     * @return cola de {@link Reserva} pendientes, vacía si no hay reservas
     */
    public Queue<Reserva> getReservas(String idRecurso) {
        return reservas.getOrDefault(idRecurso, new ConcurrentLinkedQueue<>());
    }

    /**
     * Obtiene todos los préstamos activos en el sistema.
     *
     * @return colección de todos los {@link Prestamo}
     */
    public Collection<Prestamo> getTodosLosPrestamos() {
        return prestamos.values();
    }
}