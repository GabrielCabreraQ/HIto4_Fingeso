package com.easywheels.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Informe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehiculo_id", nullable = false)
    @JsonBackReference //Evita ciclos infinitos
    private Vehiculo vehiculo;

    private String observaciones;
    private LocalDate fechaGeneracion;

    //Constructor vacío requerido por JPA
    public Informe() {
    }

    //Constructor completo
    public Informe(Vehiculo vehiculo, String observaciones) {
        this.vehiculo = vehiculo;
        this.observaciones = observaciones;
        this.fechaGeneracion = LocalDate.now();
    }

    // Metodo para determinar si el vehículo está en condiciones de uso
    public boolean esVehiculoApto() {
        // Implementar aquí la lógica que determine si el vehículo es apto o no
        return !observaciones.toLowerCase().contains("falla");
    }

    //Getters y Setters
    public Long getId() {
        return id;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

}