package com.easywheels.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@DiscriminatorValue("solicitud")
public class SolicitudCancelacion {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;  // El ID se generará automáticamente
    public int idArriendo;
    public String descripcion;
    public Boolean cancelacionRealizada = false;

    public SolicitudCancelacion(long id, int idArriendo, String descripcion, Boolean cancelacionRealizada) {
        this.id = id;  // No será necesario asignar un valor al id si se genera automáticamente
        this.idArriendo = idArriendo;
        this.descripcion = descripcion;
        this.cancelacionRealizada = cancelacionRealizada;
    }

    public SolicitudCancelacion() {
        // Constructor sin parámetros, para uso de JPA
    }

    // Ya no es necesario el setId(int id) ya que la base de datos se encarga del id.
    public void setId(long id) {
        this.id = id;
    }

    public long getIdArriendo() {
        return idArriendo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getCancelacionRealizada() {
        return cancelacionRealizada;
    }

    public void setCancelacionRealizada(Boolean cancelacionRealizada) {
        this.cancelacionRealizada = cancelacionRealizada;
    }
    public long getId() {
        return id;
    }

}
