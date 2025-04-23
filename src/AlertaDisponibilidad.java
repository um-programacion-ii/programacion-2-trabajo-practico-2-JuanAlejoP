import java.util.Queue;
import java.util.Scanner;

public class AlertaDisponibilidad {
    private GestorRecursos resourceManager;
    private GestorUsuarios userManager;
    private ServicioNotificaciones notifications;

    public AlertaDisponibilidad(GestorRecursos resourceManager,
                                GestorUsuarios userManager,
                                ServicioNotificaciones notifications) {
        this.resourceManager = resourceManager;
        this.userManager = userManager;
        this.notifications = notifications;
    }

    public void revisarDisponibilidad() {
        for (RecursoDigital recurso : resourceManager.listAllResources()) {
            String id = recurso.getIdentificador();
            if (recurso.getEstado() == EstadoRecurso.DISPONIBLE) {
                Queue<Reserva> cola = resourceManager.getReservas(id);
                if (!cola.isEmpty()) {
                    Reserva reserva = cola.peek();
                    Usuario usuario = reserva.getUsuario();
                    String mensaje = "✅ Recurso disponible: " + id;
                    notifications.enviarNotificacion(mensaje, usuario);
                    System.out.println(mensaje + " (Usuario: " + usuario.getId() + ")");
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