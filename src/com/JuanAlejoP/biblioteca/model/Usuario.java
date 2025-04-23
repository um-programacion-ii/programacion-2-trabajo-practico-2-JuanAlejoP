package com.JuanAlejoP.biblioteca.model;

/**
 * Representa un usuario registrado en la biblioteca.
 * Contiene datos de contacto y un identificador único.
 */
public class Usuario {
    /**
     * Nombre completo del usuario.
     */
    private String nombre;

    /**
     * Identificador único del usuario.
     */
    private String id;

    /**
     * Correo electrónico del usuario.
     */
    private String email;

    /**
     * Número de teléfono del usuario.
     */
    private String telefono;

    /**
     * Construye una nueva instancia de Usuario con los datos proporcionados.
     *
     * @param nombre   nombre completo del usuario
     * @param id       identificador único del usuario
     * @param email    correo electrónico de contacto
     * @param telefono número de teléfono de contacto
     */
    public Usuario(String nombre, String id, String email, String telefono) {
        this.nombre = nombre;
        this.id = id;
        this.email = email;
        this.telefono = telefono;
    }

    /**
     * Obtiene el nombre completo del usuario.
     *
     * @return nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el identificador único del usuario.
     *
     * @return ID del usuario
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return email del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Obtiene el número de teléfono del usuario.
     *
     * @return teléfono del usuario
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Actualiza el nombre completo del usuario.
     *
     * @param nombre nuevo nombre del usuario
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Actualiza el identificador único del usuario.
     *
     * @param id nuevo ID del usuario
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Actualiza el correo electrónico del usuario.
     *
     * @param email nuevo email del usuario
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Actualiza el número de teléfono del usuario.
     *
     * @param telefono nuevo teléfono del usuario
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}