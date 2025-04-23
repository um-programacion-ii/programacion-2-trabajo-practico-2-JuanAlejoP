import java.time.LocalDateTime;

public class NotificacionEntry {
    private LocalDateTime timestamp;
    private AlertType type;
    private String mensaje;
    private String usuarioId;

    public NotificacionEntry(LocalDateTime timestamp, AlertType type, String mensaje, String usuarioId) {
        this.timestamp = timestamp;
        this.type = type;
        this.mensaje = mensaje;
        this.usuarioId = usuarioId;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public AlertType getType() { return type; }
    public String getMensaje() { return mensaje; }
    public String getUsuarioId() { return usuarioId; }
}