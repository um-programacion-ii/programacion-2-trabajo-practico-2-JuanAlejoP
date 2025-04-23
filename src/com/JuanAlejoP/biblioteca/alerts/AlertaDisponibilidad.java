package com.JuanAlejoP.biblioteca.alerts;

import com.JuanAlejoP.biblioteca.manager.GestorRecursos;
import com.JuanAlejoP.biblioteca.manager.GestorUsuarios;
import com.JuanAlejoP.biblioteca.model.EstadoRecurso;
import com.JuanAlejoP.biblioteca.model.RecursoDigital;
import com.JuanAlejoP.biblioteca.model.Reserva;
import com.JuanAlejoP.biblioteca.model.Usuario;
import com.JuanAlejoP.biblioteca.service.ServicioNotificaciones;

import java.util.Queue;
import java.util.Scanner;

/**
 * La clase {@code AlertaDisponibilidad} se encarga de revisar la disponibilidad de recursos digitales
 * y gestionar las notificaciones a los usuarios en cola de reservas.
 */
public class AlertaDisponibilidad {
    /** Gestor de recursos digitales que ofrece métodos para listar y prestar recursos. */
    private GestorRecursos resourceManager;
    /** Gestor de usuarios que maneja la información de los usuarios del sistema. */
    private GestorUsuarios userManager;
    /** Servicio encargado de enviar notificaciones a los usuarios. */
    private ServicioNotificaciones notifications;

    /**
     * Crea una instancia de {@code AlertaDisponibilidad} con los gestores y servicios necesarios.
     *
     * @param resourceManager gestor de recursos digitales
     * @param userManager     gestor de usuarios
     * @param notifications   servicio para enviar notificaciones a usuarios
     */
    public AlertaDisponibilidad(GestorRecursos resourceManager,
                                GestorUsuarios userManager,
                                ServicioNotificaciones notifications) {
        this.resourceManager = resourceManager;
        this.userManager = userManager;
        this.notifications = notifications;
    }

    /**
     * Revisa todos los recursos digitales y, para cada recurso disponible, verifica si existe
     * una cola de reservas. Si la cola no está vacía, notifica al primer usuario en espera.
     * Luego solicita confirmación para realizar el préstamo al usuario y, en caso afirmativo,
     * ejecuta la operación de préstamo.
     */
    public void revisarDisponibilidad() {
        for (RecursoDigital recurso : resourceManager.listAllResources()) {
            String id = recurso.getIdentificador();
            if (recurso.getEstado() == EstadoRecurso.DISPONIBLE) {
                Queue<Reserva> cola = resourceManager.getReservas(id);
                if (!cola.isEmpty()) {
                    Reserva reserva = cola.peek();
                    Usuario usuario = reserva.getUsuario();
                    String mensaje = "✅ Recurso disponible: " + id;

                    // Envío de notificación al usuario que encabeza la cola
                    notifications.enviarNotificacion(mensaje, usuario);
                    System.out.println(mensaje + " (Usuario ID: " + usuario.getId() + ")");

                    // Solicita al usuario si desea tomar el recurso
                    System.out.print("¿Desea tomar el recurso ahora? (s/n): ");
                    Scanner sc = new Scanner(System.in);
                    if (sc.nextLine().equalsIgnoreCase("s")) {
                        try {
                            resourceManager.prestarRecurso(id, usuario);
                            cola.poll();
                            System.out.println("Préstamo registrado con éxito.");
                        } catch (Exception e) {
                            System.out.println("No se pudo prestar: " + e.getMessage());
                        }
                    }
                }
            }
        }
    }
}