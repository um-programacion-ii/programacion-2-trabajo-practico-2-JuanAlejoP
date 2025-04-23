package com.JuanAlejoP.biblioteca.alerts;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Clase que mantiene un historial sincronizado de entradas de notificaciones.
 * Utiliza una lista sincronizada para asegurar la seguridad en concurrencia.
 */
public class AlertHistory {
    /**
     * Lista sincronizada que almacena las entradas de notificación.
     */
    private static final List<NotificacionEntry> history = Collections.synchronizedList(new ArrayList<>());

    /**
     * Agrega una nueva entrada al historial de notificaciones.
     *
     * @param entry instancia de NotificacionEntry que se desea registrar en el historial
     */
    public static void addEntry(NotificacionEntry entry) {
        history.add(entry);
    }

    /**
     * Recupera el historial completo de notificaciones.
     *
     * @return nueva lista que contiene todas las entradas de notificación registradas
     */
    public static List<NotificacionEntry> getHistory() {
        return new ArrayList<>(history);
    }
}