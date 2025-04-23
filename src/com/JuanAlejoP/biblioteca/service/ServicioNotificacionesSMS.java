package com.JuanAlejoP.biblioteca.service;

import com.JuanAlejoP.biblioteca.model.Usuario;

/**
 * Implementación de {@link ServicioNotificaciones} que envía notificaciones por SMS.
 * <p>
 * Esta clase simula el envío de un mensaje de texto imprimiendo en consola
 * el número de teléfono del destinatario y el contenido del mensaje.
 * </p>
 */
public class ServicioNotificacionesSMS implements ServicioNotificaciones {

    /**
     * Envía una notificación por SMS a un usuario.
     * <p>
     * Formatea el mensaje para indicar el envío por SMS
     * e incluye el número de teléfono del usuario.
     * </p>
     *
     * @param mensaje Texto de la notificación a enviar.
     * @param usuario Usuario destinatario, del que se utiliza el teléfono.
     */
    @Override
    public void enviarNotificacion(String mensaje, Usuario usuario) {
        System.out.println("Enviando SMS a " + usuario.getTelefono() + ": " + mensaje);
    }
}