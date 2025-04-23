import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Scanner;

public class AlertaVencimiento {
    private GestorRecursos resourceManager;
    private GestorUsuarios userManager;
    private ServicioNotificaciones notifications;

    public AlertaVencimiento(GestorRecursos resourceManager,
                             GestorUsuarios userManager,
                             ServicioNotificaciones notifications) {
        this.resourceManager = resourceManager;
        this.userManager = userManager;
        this.notifications = notifications;
    }

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
                System.out.println(mensaje + " (Usuario: " + usuario.getId() + ")");
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