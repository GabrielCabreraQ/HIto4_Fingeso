package com.easywheels.Repository;

import com.easywheels.Model.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
    @Query("SELECT p FROM Publicacion p WHERE " +
            "(:marca IS NULL OR LOWER(p.vehiculo.marca) = LOWER(:marca)) AND " +
            "(:tipoTransmision IS NULL OR LOWER(p.vehiculo.tipoTransmision) = LOWER(:tipoTransmision)) AND " +
            "(:anioMin IS NULL OR p.vehiculo.anio >= :anioMin) AND " +
            "(:anioMax IS NULL OR p.vehiculo.anio <= :anioMax) AND " +
            "(:precioMin IS NULL OR p.precioNormal >= :precioMin) AND " +
            "(:precioMax IS NULL OR p.precioNormal <= :precioMax)")
    List<Publicacion> filtrarPublicaciones(
            @Param("marca") String marca,
            @Param("tipoTransmision") String tipoTransmision,
            @Param("anioMin") Integer anioMin,
            @Param("anioMax") Integer anioMax,
            @Param("precioMin") Double precioMin,
            @Param("precioMax") Double precioMax
    );
}


