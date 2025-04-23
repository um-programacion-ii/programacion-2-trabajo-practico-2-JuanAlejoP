package com.JuanAlejoP.biblioteca.model;

/**
 * Interfaz que define el contrato para todos los recursos digitales de la biblioteca.
 * Proporciona métodos para acceder a sus metadatos y actualizar su estado y categoría.
 */
public interface RecursoDigital {

    /**
     * Obtiene el identificador único del recurso.
     *
     * @return cadena con el ID del recurso
     */
    String getIdentificador();

    /**
     * Obtiene el título descriptivo del recurso.
     *
     * @return cadena con el título del recurso
     */
    String getTitulo();

    /**
     * Recupera el estado actual del recurso (disponible, prestado, reservado).
     *
     * @return estado actual del recurso
     */
    EstadoRecurso getEstado();

    /**
     * Actualiza el estado del recurso.
     *
     * @param nuevoEstado nuevo estado a asignar al recurso
     */
    void actualizarEstado(EstadoRecurso nuevoEstado);

    /**
     * Recupera la categoría asignada al recurso.
     *
     * @return categoría del recurso
     */
    CategoriaRecurso getCategoria();

    /**
     * Establece una nueva categoría para el recurso.
     *
     * @param categoria categoría a asignar al recurso
     */
    void setCategoria(CategoriaRecurso categoria);
}