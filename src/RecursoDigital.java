public interface RecursoDigital {
    public abstract String getIdentificador();

    public abstract String getTitulo();

    public abstract EstadoRecurso getEstado();

    public abstract void actualizarEstado(EstadoRecurso nuevoEstado);
}