import java.time.LocalDateTime;

public class Reserva {
    private RecursoDigital recurso;
    private Usuario usuario;
    private LocalDateTime fechaSolicitud;

    public Reserva(RecursoDigital recurso, Usuario usuario) {
        this.recurso = recurso;
        this.usuario = usuario;
        this.fechaSolicitud = LocalDateTime.now();
    }

    public RecursoDigital getRecurso() {
        return recurso;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }
}