package com.JuanAlejoP.biblioteca.service;

import com.JuanAlejoP.biblioteca.alerts.AlertType;

import java.util.concurrent.ConcurrentHashMap;
import java.util.EnumMap;

/**
 * Administrador de preferencias de notificación por usuario y tipo de alerta.
 * <p>
 * Permite habilitar o deshabilitar distintos tipos de notificaciones
 * de forma concurrente, asegurando hilos seguros mediante
 * {@link ConcurrentHashMap} y mapas de enums.
 * </p>
 */
public class PreferencesManager {

    /**
     * Mapa concurrente que almacena por cada usuario un mapa de preferencias
     * para cada {@link AlertType}.
     */
    private final ConcurrentHashMap<String, EnumMap<AlertType, Boolean>> prefs = new ConcurrentHashMap<>();

    /**
     * Establece la preferencia de notificación de un usuario para un tipo de alerta.
     * <p>
     * Si el usuario no existía en el mapa, inicializa sus preferencias
     * con todos los tipos habilitados por defecto.
     * </p>
     *
     * @param userId  Identificador del usuario.
     * @param type    Tipo de alerta cuya preferencia se modifica.
     * @param enabled Valor {@code true} para habilitar o {@code false} para deshabilitar.
     */
    public void setPreference(String userId, AlertType type, boolean enabled) {
        prefs.computeIfAbsent(userId, k -> {
            EnumMap<AlertType, Boolean> map = new EnumMap<>(AlertType.class);
            // Inicializar todas las alertas como habilitadas
            for (AlertType t : AlertType.values()) {
                map.put(t, true);
            }
            return map;
        }).put(type, enabled);
    }

    /**
     * Consulta si un tipo de alerta está habilitado para un usuario.
     * <p>
     * Si el usuario o el tipo no tienen preferencia registrada,
     * devuelve {@code true} por defecto.
     * </p>
     *
     * @param userId Identificador del usuario.
     * @param type   Tipo de alerta a consultar.
     * @return {@code true} si está habilitado, {@code false} en caso contrario.
     */
    public boolean isEnabled(String userId, AlertType type) {
        // Devuelve true si no existen preferencias previas para usuario o tipo
        return prefs.getOrDefault(userId, new EnumMap<>(AlertType.class))
                .getOrDefault(type, true);
    }
}