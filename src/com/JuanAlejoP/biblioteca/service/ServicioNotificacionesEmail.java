package com.JuanAlejoP.biblioteca.service;

import com.JuanAlejoP.biblioteca.model.Usuario;

/**
 * Implementación de {@link ServicioNotificaciones} que envía notificaciones por email.
 * <p>
 * Esta clase simula el envío de un correo electrónico imprimiendo en consola
 * el destinatario y el contenido del mensaje.
 * </p>
 */
public class ServicioNotificacionesEmail implements ServicioNotificaciones {

    /**
     * Envía una notificación mediante email a un usuario.
     * <p>
     * Formatea el mensaje para indicar el envío por correo electrónico
     * e incluye la dirección del usuario.
     * </p>
     *
     * @param mensaje Texto de la notificación a enviar.
     * @param usuario Usuario destinatario, del que se utiliza el email.
     */
    @Override
    public void enviarNotificacion(String mensaje, Usuario usuario) {
        System.out.println("Enviando email a " + usuario.getEmail() + ": " + mensaje);
    }
}