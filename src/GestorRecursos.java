import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.Queue;
import java.util.LinkedList;
import java.time.LocalDateTime;

public class GestorRecursos {
    private Map<String, RecursoDigital> recursos;
    private Map<String, Prestamo> prestamos;
    private Map<String, Queue<Reserva>> reservas;

    public GestorRecursos() {
        this.recursos = new HashMap<>();
        this.prestamos = new HashMap<>();
        this.reservas = new HashMap<>();
    }

    public void addNewResource(RecursoDigital recursoDigital) {
        String id = recursoDigital.getIdentificador();
        recursos.put(id, recursoDigital);
    }

    public RecursoDigital searchResourceById(String id) throws RecursoNoDisponibleException {
        RecursoDigital recursoDigital = recursos.get(id);
        if (recursoDigital == null) {
            throw new RecursoNoDisponibleException("Recurso con ID " + id + " no encontrado.");
        }
        return recursoDigital;
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

    // ----------- Préstamos y Reservas -----------

    public void prestarRecurso(String idRecurso, Usuario usuario) throws RecursoNoDisponibleException {
        RecursoDigital recurso = searchResourceById(idRecurso);

        if (!(recurso instanceof Prestable)) {
            throw new RecursoNoDisponibleException("Este recurso no puede ser prestado.");
        }

        if (recurso.getEstado() != EstadoRecurso.DISPONIBLE) {
            throw new RecursoNoDisponibleException("El recurso no está disponible para préstamo.");
        }

        recurso.actualizarEstado(EstadoRecurso.PRESTADO);
        Prestamo prestamo = new Prestamo(recurso, usuario);
        prestamos.put(idRecurso, prestamo);
    }

    public void devolverRecurso(String idRecurso) throws RecursoNoDisponibleException {
        RecursoDigital recurso = searchResourceById(idRecurso);

        if (!prestamos.containsKey(idRecurso)) {
            throw new RecursoNoDisponibleException("El recurso no está actualmente prestado.");
        }

        prestamos.remove(idRecurso);

        Queue<Reserva> colaReservas = reservas.get(idRecurso);
        if (colaReservas != null && !colaReservas.isEmpty()) {
            recurso.actualizarEstado(EstadoRecurso.RESERVADO);
        } else {
            recurso.actualizarEstado(EstadoRecurso.DISPONIBLE);
        }
    }

    public void reservarRecurso(String idRecurso, Usuario usuario) throws RecursoNoDisponibleException {
        RecursoDigital recurso = searchResourceById(idRecurso);

        if (!(recurso instanceof Prestable)) {
            throw new RecursoNoDisponibleException("Este recurso no puede ser reservado.");
        }

        if (recurso.getEstado() == EstadoRecurso.DISPONIBLE) {
            throw new RecursoNoDisponibleException("El recurso está disponible, no es necesario reservarlo.");
        }

        reservas.putIfAbsent(idRecurso, new LinkedList<>());
        reservas.get(idRecurso).add(new Reserva(recurso, usuario));
    }

    public void renovarRecurso(String idRecurso, Usuario usuario) throws RecursoNoDisponibleException {
        RecursoDigital recurso = searchResourceById(idRecurso);

        if (!(recurso instanceof Renovable)) {
            throw new RecursoNoDisponibleException("Este recurso no permite renovación.");
        }

        Prestamo prestamo = prestamos.get(idRecurso);
        if (prestamo == null || !prestamo.getUsuario().getId().equals(usuario.getId())) {
            throw new RecursoNoDisponibleException("El recurso no está prestado a este usuario.");
        }

        // Se renueva agregando 7 días extra
        LocalDateTime nuevaFechaDevolucion = prestamo.getFechaDevolucion().plusDays(7);
        prestamos.put(idRecurso, new Prestamo(recurso, usuario));  // recrea el préstamo con nueva fecha
    }

    public Prestamo getPrestamo(String idRecurso) {
        return prestamos.get(idRecurso);
    }

    public Queue<Reserva> getReservas(String idRecurso) {
        return reservas.getOrDefault(idRecurso, new LinkedList<>());
    }
}