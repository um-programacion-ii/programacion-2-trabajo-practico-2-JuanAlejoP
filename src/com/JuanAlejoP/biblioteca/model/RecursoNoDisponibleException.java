package com.JuanAlejoP.biblioteca.model;

/**
 * Excepción que se lanza cuando un recurso digital no está disponible
 * para la operación solicitada (búsqueda, préstamo, reserva, renovación, etc.).
 */
public class RecursoNoDisponibleException extends Exception {

    /**
     * Construye una nueva instancia de RecursoNoDisponibleException con un mensaje
     * que describe la causa de la indisponibilidad del recurso.
     *
     * @param mensaje detalle del motivo por el cual el recurso no está disponible
     */
    public RecursoNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}