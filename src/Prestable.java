import java.time.LocalDateTime;

public interface Prestable {
    public abstract boolean estaDisponible();
    public abstract LocalDateTime getFechaDevolucion();
    public abstract void prestar();
}