package com.JuanAlejoP.biblioteca.service;

import com.JuanAlejoP.biblioteca.model.Usuario;

/**
 * Interfaz que define el servicio de notificaciones para la aplicación.
 * <p>
 * Permite enviar notificaciones a un usuario con un mensaje dado.
 * </p>
 */
public interface ServicioNotificaciones {

    /**
     * Envía una notificación a un usuario específico.
     *
     * @param mensaje Texto de la notificación.
     * @param usuario Usuario que recibirá la notificación.
     */
    void enviarNotificacion(String mensaje, Usuario usuario);
}