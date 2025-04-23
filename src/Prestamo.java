import java.time.LocalDateTime;

public class Prestamo {
    private RecursoDigital recurso;
    private Usuario usuario;
    private LocalDateTime fechaPrestamo;
    private LocalDateTime fechaDevolucion;

    public Prestamo(RecursoDigital recurso, Usuario usuario) {
        this.recurso = recurso;
        this.usuario = usuario;
        this.fechaPrestamo = LocalDateTime.now();
        this.fechaDevolucion = fechaPrestamo.plusDays(14);
    }

    public RecursoDigital getRecurso() {
        return recurso;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDateTime getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDateTime getFechaDevolucion() {
        return fechaDevolucion;
    }
}