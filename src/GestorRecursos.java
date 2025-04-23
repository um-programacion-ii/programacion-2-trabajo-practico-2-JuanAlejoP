import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.Comparator;

public class GestorRecursos {
    private ConcurrentMap<String, RecursoDigital> recursos;
    private ConcurrentMap<String, Prestamo> prestamos;
    private ConcurrentMap<String, Queue<Reserva>> reservas;

    public GestorRecursos() {
        this.recursos = new ConcurrentHashMap<>();
        this.prestamos = new ConcurrentHashMap<>();
        this.reservas = new ConcurrentHashMap<>();
    }

    public void addNewResource(RecursoDigital recursoDigital) {
        recursos.put(recursoDigital.getIdentificador(), recursoDigital);
    }

    public RecursoDigital searchResourceById(String id) throws RecursoNoDisponibleException {
        RecursoDigital rd = recursos.get(id);
        if (rd == null) throw new RecursoNoDisponibleException("Recurso con ID " + id + " no encontrado.");
        return rd;
    }

    public Collection<RecursoDigital> listAllResources() {
        return recursos.values();
    }

    public List<RecursoDigital> searchByTitle(String title) {
        return recursos.values().stream()
                .filter(r -> r.getTitulo().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<RecursoDigital> filterByCategoria(CategoriaRecurso categoria) {
        return recursos.values().stream()
                .filter(r -> r instanceof RecursoBase && ((RecursoBase) r).getCategoria() == categoria)
                .collect(Collectors.toList());
    }

    public List<RecursoDigital> sortByTitulo() {
        return recursos.values().stream()
                .sorted(Comparator.comparing(RecursoDigital::getTitulo))
                .collect(Collectors.toList());
    }

    public List<RecursoDigital> sortByCategoria() {
        return recursos.values().stream()
                .filter(r -> r instanceof RecursoBase)
                .sorted(Comparator.comparing(r -> ((RecursoBase) r).getCategoria().toString()))
                .collect(Collectors.toList());
    }

    // Préstamos y Reservas
    public void prestarRecurso(String idRecurso, Usuario usuario) throws RecursoNoDisponibleException {
        RecursoDigital recurso = searchResourceById(idRecurso);
        if (!(recurso instanceof Prestable)) throw new RecursoNoDisponibleException("Este recurso no puede ser prestado.");
        if (recurso.getEstado() != EstadoRecurso.DISPONIBLE) throw new RecursoNoDisponibleException("El recurso no está disponible.");
        recurso.actualizarEstado(EstadoRecurso.PRESTADO);
        prestamos.put(idRecurso, new Prestamo(recurso, usuario));
    }

    public void devolverRecurso(String idRecurso) throws RecursoNoDisponibleException {
        RecursoDigital recurso = searchResourceById(idRecurso);
        if (!prestamos.containsKey(idRecurso)) throw new RecursoNoDisponibleException("Recurso no está prestado.");
        prestamos.remove(idRecurso);
        Queue<Reserva> cola = reservas.get(idRecurso);
        if (cola != null && !cola.isEmpty()) {
            recurso.actualizarEstado(EstadoRecurso.RESERVADO);
        } else {
            recurso.actualizarEstado(EstadoRecurso.DISPONIBLE);
        }
    }

    public void reservarRecurso(String idRecurso, Usuario usuario) throws RecursoNoDisponibleException {
        RecursoDigital recurso = searchResourceById(idRecurso);
        if (!(recurso instanceof Prestable)) throw new RecursoNoDisponibleException("Este recurso no puede reservarse.");
        if (recurso.getEstado() == EstadoRecurso.DISPONIBLE) throw new RecursoNoDisponibleException("Recurso disponible, reserva no necesaria.");
        reservas.computeIfAbsent(idRecurso, k -> new ConcurrentLinkedQueue<>()).add(new Reserva(recurso, usuario));
    }

    public void renovarRecurso(String idRecurso, Usuario usuario) throws RecursoNoDisponibleException {
        RecursoDigital recurso = searchResourceById(idRecurso);
        if (!(recurso instanceof Renovable)) throw new RecursoNoDisponibleException("Este recurso no permite renovación.");
        Prestamo prestamo = prestamos.get(idRecurso);
        if (prestamo == null || !prestamo.getUsuario().getId().equals(usuario.getId())) throw new RecursoNoDisponibleException("No es su préstamo.");
        prestamos.put(idRecurso, new Prestamo(recurso, usuario));
    }

    public Prestamo getPrestamo(String idRecurso) {
        return prestamos.get(idRecurso);
    }

    public Queue<Reserva> getReservas(String idRecurso) {
        return reservas.getOrDefault(idRecurso, new ConcurrentLinkedQueue<>());
    }

    public Collection<Prestamo> getTodosLosPrestamos() {
        return prestamos.values();
    }
}