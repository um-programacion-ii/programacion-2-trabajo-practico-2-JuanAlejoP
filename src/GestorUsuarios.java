import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

public class GestorUsuarios {
    private Map<String, Usuario> usuarios;
    private ServicioNotificaciones servicioNotificaciones;

    public GestorUsuarios(ServicioNotificaciones servicioNotificaciones) {
        this.usuarios = new HashMap<>();
        this.servicioNotificaciones = servicioNotificaciones;
    }

    public void addNewUser(Usuario usuario) {
        String id = usuario.getId();
        usuarios.put(id, usuario);
    }

    public Usuario searchUserById(String id) {
        return usuarios.get(id);
    }

    public Collection<Usuario> listAllUsers() {
        return usuarios.values();
    }

    public void notificarUsuario(String mensaje, Usuario usuario) {
        servicioNotificaciones.enviarNotificacion(mensaje, usuario);
    }
}