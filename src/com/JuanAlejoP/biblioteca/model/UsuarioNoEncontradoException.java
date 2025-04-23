package com.JuanAlejoP.biblioteca.model;

/**
 * Excepción que se lanza cuando un usuario no es encontrado en la biblioteca.
 * Extiende de Exception para representar un error de búsqueda de usuario.
 */
public class UsuarioNoEncontradoException extends Exception {

    /**
     * Construye una nueva excepción con un mensaje detallado.
     *
     * @param mensaje Descripción del motivo por el cual no se encontró el usuario.
     */
    public UsuarioNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}