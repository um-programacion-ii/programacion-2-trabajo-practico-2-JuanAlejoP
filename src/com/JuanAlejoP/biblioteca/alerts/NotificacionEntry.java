package com.JuanAlejoP.biblioteca.alerts;

import java.time.LocalDateTime;

/**
 * Representa una entrada de notificación en el sistema de alertas.
 * Contiene la información básica de cada notificación enviada a un usuario.
 */
public class NotificacionEntry {
    /**
     * Fecha y hora en que se generó la notificación.
     */
    private LocalDateTime timestamp;

    /**
     * Tipo de alerta (INFO, WARNING, ERROR) asociado a esta notificación.
     */
    private AlertType type;

    /**
     * Mensaje de texto de la notificación.
     */
    private String mensaje;

    /**
     * Identificador del usuario destinatario de la notificación.
     */
    private String usuarioId;

    /**
     * Construye una nueva entrada de notificación con los datos proporcionados.
     *
     * @param timestamp fecha y hora de la notificación
     * @param type      tipo de alerta
     * @param mensaje   texto del mensaje de la notificación
     * @param usuarioId identificador único del usuario destinatario
     */
    public NotificacionEntry(LocalDateTime timestamp, AlertType type, String mensaje, String usuarioId) {
        this.timestamp = timestamp;
        this.type = type;
        this.mensaje = mensaje;
        this.usuarioId = usuarioId;
    }

    /**
     * Recupera la fecha y hora en que se generó la notificación.
     *
     * @return timestamp de la notificación
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Recupera el tipo de alerta de la notificación.
     *
     * @return tipo de alerta
     */
    public AlertType getType() {
        return type;
    }

    /**
     * Recupera el mensaje de la notificación.
     *
     * @return texto del mensaje
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Recupera el identificador del usuario destinatario.
     *
     * @return ID del usuario
     */
    public String getUsuarioId() {
        return usuarioId;
    }
}