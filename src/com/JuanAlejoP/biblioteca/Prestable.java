package com.JuanAlejoP.biblioteca;

import java.time.LocalDateTime;

/**
 * Representa entidades que pueden ser prestadas y devueltas.
 * <p>
 * Define los métodos necesarios para consultar disponibilidad,
 * gestionar préstamos y obtener la fecha de devolución.
 * </p>
 */
public interface Prestable {

    /**
     * Indica si el recurso está disponible para préstamo.
     *
     * @return {@code true} si está disponible, {@code false} si ya está prestado o reservado.
     */
    boolean estaDisponible();

    /**
     * Obtiene la fecha y hora estimada de devolución del recurso.
     *
     * @return Instancia de {@link LocalDateTime} con la fecha de devolución,
     *         o {@code null} si no está prestado.
     */
    LocalDateTime getFechaDevolucion();

    /**
     * Marca el recurso como prestado y actualiza la fecha de devolución.
     * <p>
     * Implementaciones deberían ajustar internamente estado y fecha.
     * </p>
     */
    void prestar();
}