package com.JuanAlejoP.biblioteca.service;

import com.JuanAlejoP.biblioteca.alerts.AlertHistory;
import com.JuanAlejoP.biblioteca.alerts.AlertType;
import com.JuanAlejoP.biblioteca.alerts.NotificacionEntry;
import com.JuanAlejoP.biblioteca.model.Usuario;
import java.time.LocalDateTime;

/**
 * Servicio de notificaciones que envía mensajes a la consola.
 * <p>
 * Determina el tipo de alerta según el prefijo del mensaje y verifica
 * las preferencias del usuario antes de mostrar la notificación.
 * Además, registra cada notificación en el historial de alertas.
 * </p>
 */
public class NotificadorConsola implements ServicioNotificaciones {

    /**
     * Gestor de preferencias para habilitar o deshabilitar alertas por usuario.
     */
    private final PreferencesManager preferences;

    /**
     * Construye un NotificadorConsola con el administrador de preferencias.
     *
     * @param preferences Objeto que controla las preferencias de notificación por usuario.
     */
    public NotificadorConsola(PreferencesManager preferences) {
        this.preferences = preferences;
    }

    /**
     * Envía una notificación a un usuario específico.
     * <p>
     * Determina el tipo de alerta (INFO o WARNING) según si el mensaje comienza
     * con "⚠️". Si el usuario tiene deshabilitado ese tipo de alerta, no se envía.
     * En caso contrario, imprime el mensaje formateado y lo registra en el historial.
     * </p>
     *
     * @param mensaje Texto de la notificación, puede incluir prefijo indicativo de nivel.
     * @param usuario Objeto usuario que recibirá la notificación.
     */
    @Override
    public void enviarNotificacion(String mensaje, Usuario usuario) {
        AlertType type = mensaje.startsWith("⚠️")
                ? AlertType.WARNING
                : AlertType.INFO;
        // No enviar si el usuario ha deshabilitado este tipo de alerta
        if (!preferences.isEnabled(usuario.getId(), type)) {
            return;
        }
        // Mostrar en consola y registrar en historial
        System.out.println(formatByType(type, mensaje));
        AlertHistory.addEntry(
                new NotificacionEntry(
                        LocalDateTime.now(),
                        type,
                        mensaje,
                        usuario.getId()
                )
        );
    }

    /**
     * Formatea el mensaje según el tipo de alerta.
     *
     * @param type Tipo de alerta (INFO, WARNING, ERROR, etc.).
     * @param msg  Mensaje original sin formato.
     * @return Cadena formateada con prefijo adecuado.
     */
    private String formatByType(AlertType type, String msg) {
        switch (type) {
            case WARNING:
                return "[WARN] " + msg;
            case ERROR:
                return "[ERROR] " + msg;
            default:
                return "[INFO] " + msg;
        }
    }
}