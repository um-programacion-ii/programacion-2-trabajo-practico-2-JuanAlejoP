package com.JuanAlejoP.biblioteca.model;

/**
 * Enumeración de categorías de recursos disponibles en la biblioteca.
 * <ul>
 *   <li>{@link #LIBRO} para libros impresos o digitales.</li>
 *   <li>{@link #REVISTA} para publicaciones periódicas.</li>
 *   <li>{@link #AUDIO} para recursos sonoros, como audiolibros.</li>
 *   <li>{@link #OTRO} para cualquier otro tipo de recurso no clasificado en las anteriores categorías.</li>
 * </ul>
 */
public enum CategoriaRecurso {
    /**
     * Recursos de tipo libro, tanto físicos como electrónicos.
     */
    LIBRO,

    /**
     * Recursos correspondientes a revistas o publicaciones periódicas.
     */
    REVISTA,

    /**
     * Recursos de audio, tales como audiolibros o grabaciones.
     */
    AUDIO,

    /**
     * Categoría genérica para otros tipos de recursos no contemplados.
     */
    OTRO;
}