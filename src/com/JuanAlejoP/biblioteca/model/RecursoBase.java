package com.JuanAlejoP.biblioteca.model;

/**
 * Clase base abstracta para recursos digitales en la biblioteca.
 * Implementa los atributos y métodos comunes de un recurso.
 */
public abstract class RecursoBase implements RecursoDigital {
    /**
     * Identificador único del recurso.
     */
    private String id;

    /**
     * Título descriptivo del recurso.
     */
    private String titulo;

    /**
     * Estado actual del recurso (disponible, prestado, reservado).
     */
    private EstadoRecurso estado;

    /**
     * Categoría a la que pertenece el recurso.
     */
    private CategoriaRecurso categoria;

    /**
     * Constructor que inicializa un recurso con su identificador, título y estado.
     * La categoría por defecto se establece en OTRO.
     *
     * @param id      identificador único del recurso
     * @param titulo  título del recurso
     * @param estado  estado inicial del recurso
     */
    public RecursoBase(String id, String titulo, EstadoRecurso estado) {
        this.id = id;
        this.titulo = titulo;
        this.estado = estado;
        this.categoria = CategoriaRecurso.OTRO;
    }

    /**
     * Obtiene el identificador único del recurso.
     *
     * @return identificador del recurso
     */
    public String getIdentificador() {
        return id;
    }

    /**
     * Obtiene el título del recurso.
     *
     * @return título del recurso
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Obtiene el estado actual del recurso.
     *
     * @return estado del recurso
     */
    public EstadoRecurso getEstado() {
        return estado;
    }

    /**
     * Obtiene la categoría asignada al recurso.
     *
     * @return categoría del recurso
     */
    public CategoriaRecurso getCategoria() {
        return categoria;
    }

    /**
     * Asigna una nueva categoría al recurso.
     *
     * @param categoria nueva categoría a asignar
     */
    public void setCategoria(CategoriaRecurso categoria) {
        this.categoria = categoria;
    }

    /**
     * Actualiza el estado del recurso.
     *
     * @param nuevoEstado nuevo estado a asignar
     */
    public void actualizarEstado(EstadoRecurso nuevoEstado) {
        this.estado = nuevoEstado;
    }
}