package com.JuanAlejoP.biblioteca.manager;

import com.JuanAlejoP.biblioteca.model.Usuario;
import com.JuanAlejoP.biblioteca.service.ServicioNotificaciones;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Gestiona la información de usuarios y facilita la notificación de eventos relacionados.
 * Utiliza una estructura de datos concurrente para permitir acceso seguro.
 */
public class GestorUsuarios {
    /**
     * Mapa concurrente que almacena usuarios, indexados por su identificador único.
     */
    private ConcurrentMap<String, Usuario> usuarios;

    /**
     * Servicio de notificaciones para enviar mensajes a los usuarios.
     */
    private ServicioNotificaciones servicioNotificaciones;

    /**
     * Inicializa el gestor de usuarios con el servicio de notificaciones proporcionado.
     *
     * @param servicioNotificaciones servicio utilizado para enviar notificaciones a los usuarios
     */
    public GestorUsuarios(ServicioNotificaciones servicioNotificaciones) {
        this.usuarios = new ConcurrentHashMap<>();
        this.servicioNotificaciones = servicioNotificaciones;
    }

    /**
     * Agrega un nuevo usuario al sistema.
     *
     * @param usuario instancia de {@link Usuario} a registrar
     */
    public void addNewUser(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
    }

    /**
     * Busca un usuario por su identificador.
     *
     * @param id identificador único del usuario
     * @return instancia de {@link Usuario} si existe, o null de lo contrario
     */
    public Usuario searchUserById(String id) {
        return usuarios.get(id);
    }

    /**
     * Devuelve todos los usuarios registrados en el sistema.
     *
     * @return colección de {@link Usuario}
     */
    public Collection<Usuario> listAllUsers() {
        return usuarios.values();
    }

    /**
     * Envía una notificación a un usuario específico.
     *
     * @param mensaje mensaje que se desea enviar
     * @param usuario destinatario de la notificación
     */
    public void notificarUsuario(String mensaje, Usuario usuario) {
        servicioNotificaciones.enviarNotificacion(mensaje, usuario);
    }
}