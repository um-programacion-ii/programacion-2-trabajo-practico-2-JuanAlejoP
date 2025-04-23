package com.JuanAlejoP.biblioteca.model;

import java.time.LocalDateTime;

/**
 * Representa una reserva de un recurso digital en la biblioteca.
 * Contiene información sobre el recurso reservado, el usuario que la solicitó
 * y la fecha en que se realizó la solicitud.
 */
public class Reserva {
    /**
     * Recurso digital que está siendo reservado.
     */
    private RecursoDigital recurso;

    /**
     * Usuario que realizó la reserva.
     */
    private Usuario usuario;

    /**
     * Fecha y hora en que se solicitó la reserva.
     */
    private LocalDateTime fechaSolicitud;

    /**
     * Crea una nueva reserva para el recurso y usuario indicados.
     * La fecha de solicitud se establece en el momento de la creación.
     *
     * @param recurso recurso digital a reservar
     * @param usuario usuario que solicita la reserva
     */
    public Reserva(RecursoDigital recurso, Usuario usuario) {
        this.recurso = recurso;
        this.usuario = usuario;
        this.fechaSolicitud = LocalDateTime.now();
    }

    /**
     * Obtiene el recurso asociado a esta reserva.
     *
     * @return recurso digital reservado
     */
    public RecursoDigital getRecurso() {
        return recurso;
    }

    /**
     * Obtiene el usuario que realizó la reserva.
     *
     * @return usuario solicitante de la reserva
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Obtiene la fecha y hora de la solicitud de reserva.
     *
     * @return timestamp de la reserva
     */
    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }
}