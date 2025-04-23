package com.JuanAlejoP.biblioteca.ui;

import com.JuanAlejoP.biblioteca.service.ServicioNotificaciones;
import com.JuanAlejoP.biblioteca.service.ServicioNotificacionesEmail;

/**
 * Punto de entrada de la aplicación de la biblioteca.
 * <p>
 * Inicializa el sistema de notificaciones y la consola de interacción.
 * </p>
 */
public class Main {

    /**
     * Método principal que arranca la interfaz de consola.
     *
     * @param args Parámetros de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        ServicioNotificaciones servicio = new ServicioNotificacionesEmail();
        Consola consola = new Consola();
        consola.init();
    }
}