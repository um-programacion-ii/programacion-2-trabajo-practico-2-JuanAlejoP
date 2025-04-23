package com.JuanAlejoP.biblioteca.model;

/**
 * Enumeración que representa los posibles estados de un recurso en la biblioteca.
 * <ul>
 *   <li>{@link #DISPONIBLE}: el recurso está libre para préstamo o consulta.</li>
 *   <li>{@link #PRESTADO}: el recurso se encuentra actualmente prestado a un usuario.</li>
 *   <li>{@link #RESERVADO}: el recurso está reservado y no disponible para préstamo inmediato.</li>
 * </ul>
 */
public enum EstadoRecurso {
    /**
     * Indica que el recurso está disponible para ser prestado o utilizado.
     */
    DISPONIBLE,

    /**
     * Indica que el recurso ha sido prestado y no puede utilizarse hasta su devolución.
     */
    PRESTADO,

    /**
     * Indica que el recurso ha sido reservado por un usuario y estará disponible una vez
     * finalizado el préstamo actual o liberado según la política de reservas.
     */
    RESERVADO
}