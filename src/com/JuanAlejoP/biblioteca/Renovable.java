package com.JuanAlejoP.biblioteca;

/**
 * Representa recursos que pueden renovarse después de un préstamo.
 * <p>
 * Permite verificar si es posible renovar y realizar la acción de renovación.
 * </p>
 */
public interface Renovable {

    /**
     * Indica si el recurso aún puede renovarse según políticas.
     *
     * @return {@code true} si es renovable, {@code false} si excedió renovaciones máximas.
     */
    boolean esRenovable();

    /**
     * Obtiene el número de veces que el recurso ha sido renovado.
     *
     * @return Entero con el contador de renovaciones.
     */
    int getVecesRenovado();

    /**
     * Realiza la renovación del recurso, incrementando el contador
     * y extendiendo la fecha de devolución.
     */
    void renovar();
}