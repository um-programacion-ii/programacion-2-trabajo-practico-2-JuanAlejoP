import java.util.Map;
import java.util.HashMap;
import java.util.Collection;


public class GestorRecursos {
    private Map<String, RecursoDigital> recursos;

    public GestorRecursos() {
        this.recursos = new HashMap<>();
    }

    public void addNewResource(RecursoDigital recursoDigital) {
        String id = recursoDigital.getIdentificador();
        recursos.put(id, recursoDigital);
    }

    public RecursoDigital searchResourceById(String id) {
        RecursoDigital recursoDigital = recursos.get(id);
        return recursoDigital;
    }

    public Collection<RecursoDigital> listAllResources() {
        return recursos.values();
    }
}