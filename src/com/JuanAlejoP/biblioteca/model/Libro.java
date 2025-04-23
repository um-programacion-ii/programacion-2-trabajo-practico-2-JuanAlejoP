package com.JuanAlejoP.biblioteca.model;

import com.JuanAlejoP.biblioteca.Prestable;
import com.JuanAlejoP.biblioteca.Renovable;

import java.time.LocalDateTime;

public class Libro extends RecursoBase implements Prestable, Renovable {
    public Libro(String id, String titulo, EstadoRecurso estado) {
        super(id, titulo, estado);
    }

    @Override
    public boolean estaDisponible() {
        return false;
    }

    @Override
    public LocalDateTime getFechaDevolucion() {
        return null;
    }

    @Override
    public void prestar() {
    }

    @Override
    public boolean esRenovable() {
        return false;
    }

    @Override
    public int getVecesRenovado() {
        return 0;
    }

    @Override
    public void renovar() {
    }
}