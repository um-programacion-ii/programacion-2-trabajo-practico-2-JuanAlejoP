import java.util.Map;
import java.util.HashMap;
import java.util.Collection;


public class GestorUsuarios {
    private Map<String, Usuario> usuarios;

    public GestorUsuarios() {
        this.usuarios = new HashMap<>();
    }

    public void addNewUser(Usuario usuario) {
        String id = usuario.getId();
        usuarios.put(id, usuario);
    }

    public Usuario searchUserById(String id) {
        Usuario usuario = usuarios.get(id);
        return usuario;
    }

    public Collection<Usuario> listAllUsers() {
        return usuarios.values();
    }
}