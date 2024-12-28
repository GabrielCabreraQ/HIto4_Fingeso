package com.easywheels.Model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Documento {
    private String nombre;
    private boolean validacion;

    public Documento() {
    }

    public Documento(String nombre, boolean validacion) {
        this.nombre = nombre;
        this.validacion = validacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setValidacion(boolean validacion) {
        this.validacion = validacion;
    }
}
