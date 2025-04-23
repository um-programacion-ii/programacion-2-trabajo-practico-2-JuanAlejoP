public class ServicioNotificacionesSMS implements ServicioNotificaciones {
    @Override
    public void enviarNotificacion(String mensaje, Usuario usuario) {
        System.out.println("Enviando SMS a " + usuario.getTelefono() + ": " + mensaje);
    }
}
