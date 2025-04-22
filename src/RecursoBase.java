public abstract class RecursoBase implements RecursoDigital {
    private String id;
    private String titulo;
    private EstadoRecurso estado;

    public RecursoBase(String id, String titulo, EstadoRecurso estado) {
        this.id = id;
        this.titulo = titulo;
        this.estado = estado;
    }

    public String getIdentificador() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public EstadoRecurso getEstado() {
        return estado;
    }

    public void actualizarEstado(EstadoRecurso nuevoEstado) {
        this.estado = nuevoEstado;
    };
}