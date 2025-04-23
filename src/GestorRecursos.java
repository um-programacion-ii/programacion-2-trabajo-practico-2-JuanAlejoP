import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

public class GestorRecursos {
    private Map<String, RecursoDigital> recursos;

    public GestorRecursos() {
        this.recursos = new HashMap<>();
    }

    public void addNewResource(RecursoDigital recursoDigital) {
        String id = recursoDigital.getIdentificador();
        recursos.put(id, recursoDigital);
    }

    public RecursoDigital searchResourceById(String id) throws RecursoNoDisponibleException {
        RecursoDigital recursoDigital = recursos.get(id);
        if (recursoDigital == null) {
            throw new RecursoNoDisponibleException("Recurso con ID " + id + " no encontrado.");
        }
        return recursoDigital;
    }

    public Collection<RecursoDigital> listAllResources() {
        return recursos.values();
    }

    public List<RecursoDigital> searchByTitle(String title) {
        return recursos.values().stream()
                .filter(r -> r.getTitulo().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<RecursoDigital> filterByCategoria(CategoriaRecurso categoria) {
        return recursos.values().stream()
                .filter(r -> r instanceof RecursoBase && ((RecursoBase) r).getCategoria() == categoria)
                .collect(Collectors.toList());
    }

    public List<RecursoDigital> sortByTitulo() {
        return recursos.values().stream()
                .sorted(Comparator.comparing(RecursoDigital::getTitulo))
                .collect(Collectors.toList());
    }

    public List<RecursoDigital> sortByCategoria() {
        return recursos.values().stream()
                .filter(r -> r instanceof RecursoBase)
                .sorted(Comparator.comparing(r -> ((RecursoBase) r).getCategoria().toString()))
                .collect(Collectors.toList());
    }
}