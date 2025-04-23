package com.JuanAlejoP.biblioteca.alerts;

import com.JuanAlejoP.biblioteca.Renovable;
import com.JuanAlejoP.biblioteca.manager.GestorRecursos;
import com.JuanAlejoP.biblioteca.manager.GestorUsuarios;
import com.JuanAlejoP.biblioteca.model.Prestamo;
import com.JuanAlejoP.biblioteca.model.Usuario;
import com.JuanAlejoP.biblioteca.service.ServicioNotificaciones;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Scanner;

/**
 * Clase encargada de revisar los préstamos próximos a vencer y notificar a los usuarios.
 */
public class AlertaVencimiento {
    /** Gestor de recursos para operaciones de préstamo y renovación. */
    private GestorRecursos resourceManager;
    private GestorUsuarios userManager;
    /** Servicio para enviar notificaciones a los usuarios. */
    private ServicioNotificaciones notifications;

    /**
     * Inicializa la alerta de vencimiento con los gestores y servicios necesarios.
     *
     * @param resourceManager gestor de recursos para acceder y modificar préstamos
     * @param userManager     gestor de usuarios del sistema (actualmente no utilizado)
     * @param notifications   servicio para enviar notificaciones a los usuarios
     */
    public AlertaVencimiento(GestorRecursos resourceManager,
                             GestorUsuarios userManager,
                             ServicioNotificaciones notifications) {
        this.resourceManager = resourceManager;
        this.userManager = userManager;
        this.notifications = notifications;
    }

    /**
     * Revisa todos los préstamos y:
     * <ul>
     *     <li>Si un préstamo vence mañana, envía una notificación de alerta.</li>
     *     <li>Si un préstamo vence hoy, envía una notificación y, si el recurso es renovable,
     *     solicita al usuario si desea renovarlo.</li>
     * </ul>
     */
    public void revisarVencimientos() {
        Collection<Prestamo> prestamos = resourceManager.getTodosLosPrestamos();
        LocalDateTime ahora = LocalDateTime.now();
        for (Prestamo prestamo : prestamos) {
            LocalDateTime devolucion = prestamo.getFechaDevolucion();
            long dias = java.time.Duration.between(
                    ahora.toLocalDate().atStartOfDay(),
                    devolucion.toLocalDate().atStartOfDay()
            ).toDays();
            if (dias == 1 || dias == 0) {
                Usuario usuario = prestamo.getUsuario();
                String mensaje = (dias == 1 ? "⚠️ Su préstamo vence mañana: "
                        : "⚠️ Su préstamo vence hoy: ")
                        + prestamo.getRecurso().getIdentificador();
                notifications.enviarNotificacion(mensaje, usuario);
                System.out.println(mensaje + " (Usuario ID: " + usuario.getId() + ")");
                // Si vence hoy y es renovable, ofrecer renovación
                if (dias == 0 && prestamo.getRecurso() instanceof Renovable) {
                    System.out.print("¿Desea renovar ahora? (s/n): ");
                    Scanner sc = new Scanner(System.in);
                    if (sc.nextLine().equalsIgnoreCase("s")) {
                        try {
                            resourceManager.renovarRecurso(
                                    prestamo.getRecurso().getIdentificador(), usuario
                            );
                            System.out.println("Recurso renovado con éxito.");
                        } catch (Exception e) {
                            System.out.println("No se pudo renovar: " + e.getMessage());
                        }
                    }
                }
            }
        }
    }
}