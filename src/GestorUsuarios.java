import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class GestorUsuarios {
    private ConcurrentMap<String, Usuario> usuarios;
    private ServicioNotificaciones servicioNotificaciones;

    public GestorUsuarios(ServicioNotificaciones servicioNotificaciones) {
        this.usuarios = new ConcurrentHashMap<>();
        this.servicioNotificaciones = servicioNotificaciones;
    }

    public void addNewUser(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
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