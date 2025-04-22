import java.time.LocalDateTime;

public class Audiolibro extends RecursoBase implements Prestable {
    public Audiolibro(String id, String titulo, EstadoRecurso estado) {
        super(id, titulo, estado);
    }

    @Override
    public boolean estaDisponible() {
        return false;
    }

    @Override
    public LocalDateTime getFechaDevolucion() {
        return null;
    }

    @Override
    public void prestar() {
    }
}