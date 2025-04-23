package com.JuanAlejoP.biblioteca.model;

/**
 * Representa una publicación periódica de tipo revista en la biblioteca.
 * Hereda de {@link RecursoBase} para gestionar propiedades comunes de recursos.
 */
public class Revista extends RecursoBase {
    /**
     * Construye una nueva instancia de Revista con identificador, título y estado inicial.
     *
     * @param id      identificador único de la revista
     * @param titulo  título de la revista
     * @param estado  estado inicial de la revista (por ejemplo, DISPONIBLE, PRESTADO, RESERVADO)
     */
    public Revista(String id, String titulo, EstadoRecurso estado) {
        super(id, titulo, estado);
    }
}