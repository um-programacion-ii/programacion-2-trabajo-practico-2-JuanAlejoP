package com.JuanAlejoP.biblioteca.model;

import java.time.LocalDateTime;

/**
 * Representa el registro de un préstamo de un recurso digital a un usuario.
 * Contiene información sobre la fecha de inicio y la fecha de devolución prevista.
 */
public class Prestamo {
    /**
     * Recurso digital prestado.
     */
    private RecursoDigital recurso;

    /**
     * Usuario que solicita el préstamo.
     */
    private Usuario usuario;

    /**
     * Fecha y hora en que se realizó el préstamo.
     */
    private LocalDateTime fechaPrestamo;

    /**
     * Fecha y hora prevista para la devolución del recurso.
     */
    private LocalDateTime fechaDevolucion;

    /**
     * Crea un nuevo préstamo para el recurso y usuario indicados.
     * La fecha de préstamo se establece en el momento de la creación,
     * y la fecha de devolución se fija a 14 días después.
     *
     * @param recurso recurso digital que se presta
     * @param usuario usuario que recibe el préstamo
     */
    public Prestamo(RecursoDigital recurso, Usuario usuario) {
        this.recurso = recurso;
        this.usuario = usuario;
        this.fechaPrestamo = LocalDateTime.now();
        this.fechaDevolucion = fechaPrestamo.plusDays(14);
    }

    /**
     * Obtiene el recurso digital asociado a este préstamo.
     *
     * @return instancia de {@link RecursoDigital} prestada
     */
    public RecursoDigital getRecurso() {
        return recurso;
    }

    /**
     * Obtiene el usuario que realizó el préstamo.
     *
     * @return {@link Usuario} destinatario del recurso
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Recupera la fecha y hora en que se inició el préstamo.
     *
     * @return {@link LocalDateTime} de inicio del préstamo
     */
    public LocalDateTime getFechaPrestamo() {
        return fechaPrestamo;
    }

    /**
     * Recupera la fecha y hora previstas para la devolución del recurso.
     *
     * @return {@link LocalDateTime} de devolución prevista
     */
    public LocalDateTime getFechaDevolucion() {
        return fechaDevolucion;
    }
}