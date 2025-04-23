package com.JuanAlejoP.biblioteca.model;

import com.JuanAlejoP.biblioteca.Prestable;
import java.time.LocalDateTime;

/**
 * Representa un recurso de tipo audiolibro en la biblioteca.
 * Hereda de {@link RecursoBase} y es prestable.
 */
public class Audiolibro extends RecursoBase implements Prestable {
    /**
     * Construye un nuevo audiolibro con identificador, título y estado inicial.
     *
     * @param id      identificador único del recurso
     * @param titulo  título descriptivo del audiolibro
     * @param estado  estado inicial del recurso (por ejemplo, DISPONIBLE o PRESTADO)
     */
    public Audiolibro(String id, String titulo, EstadoRecurso estado) {
        super(id, titulo, estado);
    }

    /**
     * Indica si el audiolibro está actualmente disponible para préstamo.
     *
     * @return {@code true} si el estado del recurso es {@link EstadoRecurso#DISPONIBLE},
     *         {@code false} en caso contrario
     */
    @Override
    public boolean estaDisponible() {
        // Implementación pendiente: debe verificar el estado real del recurso
        return false;
    }

    /**
     * Obtiene la fecha y hora de devolución establecida para este préstamo.
     *
     * @return fecha y hora de devolución, o {@code null} si no se ha prestado
     */
    @Override
    public LocalDateTime getFechaDevolucion() {
        // Implementación pendiente: debería devolver la fecha calculada al prestar
        return null;
    }

    /**
     * Marca el inicio de un préstamo de este audiolibro.
     * Debería actualizar el estado y registrar la fecha de devolución.
     */
    @Override
    public void prestar() {
        // Implementación pendiente: actualizar estado y fecha de devolución
    }
}