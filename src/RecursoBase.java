public abstract class RecursoBase implements RecursoDigital {
    private String id;
    private String titulo;
    private EstadoRecurso estado;
    private CategoriaRecurso categoria;

    public RecursoBase(String id, String titulo, EstadoRecurso estado) {
        this.id = id;
        this.titulo = titulo;
        this.estado = estado;
        this.categoria = CategoriaRecurso.OTRO;
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

    public CategoriaRecurso getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaRecurso categoria) {
        this.categoria = categoria;
    }

    public void actualizarEstado(EstadoRecurso nuevoEstado) {
        this.estado = nuevoEstado;
    }
}