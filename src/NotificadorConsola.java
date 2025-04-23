import java.time.LocalDateTime;

public class NotificadorConsola implements ServicioNotificaciones {
    private PreferencesManager preferences;

    public NotificadorConsola(PreferencesManager preferences) {
        this.preferences = preferences;
    }

    @Override
    public void enviarNotificacion(String mensaje, Usuario usuario) {
        AlertType type = mensaje.startsWith("⚠️") ? AlertType.WARNING : AlertType.INFO;
        if (!preferences.isEnabled(usuario.getId(), type)) return;
        System.out.println(formatByType(type, mensaje));
        AlertHistory.addEntry(new NotificacionEntry(LocalDateTime.now(), type, mensaje, usuario.getId()));
    }

    private String formatByType(AlertType type, String msg) {
        switch (type) {
            case WARNING: return "[WARN] " + msg;
            case ERROR:   return "[ERROR] " + msg;
            default:      return "[INFO] " + msg;
        }
    }
}