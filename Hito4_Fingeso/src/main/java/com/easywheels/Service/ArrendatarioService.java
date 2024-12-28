package com.easywheels.Service;

import com.easywheels.Model.*;
import com.easywheels.Repository.ArrendatarioRepository;
import com.easywheels.Repository.ArriendoRepository;
import com.easywheels.Repository.PublicacionRepository;
import com.easywheels.Repository.VehiculoRepository;
import com.easywheels.Repository.BoletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArrendatarioService {

    @Autowired
    private ArrendatarioRepository arrendatarioRepository; // Este es el repositorio para acceder a los arrendatarios

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private ArriendoRepository arriendoRepository;

    @Autowired
    private BoletaRepository boletaRepository;  // Inyección del repositorio Boleta

    @Autowired
    private VehiculoRepository vehiculoRepository;

    // Registrar usuario
    public Arrendatario register(String rut, String nombre, String correo, String contrasenia, String telefono, LocalDate fechaNacimiento, List<String> tipoLicencia, List<String> documentos) {
        Arrendatario nuevoUsuario = new Arrendatario(rut, nombre, correo, contrasenia, telefono, fechaNacimiento, tipoLicencia, documentos);
        Arrendatario existente = arrendatarioRepository.findByCorreoUsuario(nuevoUsuario.getCorreoUsuario());
        if (existente != null) {
            return null;
        }
        return arrendatarioRepository.save(nuevoUsuario);
    }

    public boolean verificarDisponibilidad(Vehiculo vehiculo, LocalDate fechaInicio, LocalDate fechaFinal) {
        // Consultar en la base de datos los arriendos para este vehículo
        List<Arriendo> arriendos = arriendoRepository.findByVehiculoId(vehiculo.getIdVehiculo());

        // Verificar si hay conflictos de fechas
        for (Arriendo arriendo : arriendos) {
            LocalDate inicioArriendo = arriendo.getFechaInicio();
            LocalDate finArriendo = arriendo.getFechaFin();

            // Si hay cualquier intersección entre el rango solicitado y un arriendo existente, no está disponible
            if (!(fechaFinal.isBefore(inicioArriendo) || fechaInicio.isAfter(finArriendo))) {
                return false;
            }
        }

        // Si no hay conflictos, el vehículo está disponible
        return true;
    }

    public ResponseEntity<?> arrendarVehiculo(long idArrendatario, long idPublicacion, LocalDate fechaInicio, LocalDate fechaFinal) {
        try {
            // Lógica existente para validar arrendatario y publicación
            Arrendatario arrendatario = arrendatarioRepository.findById(idArrendatario)
                    .orElseThrow(() -> new IllegalArgumentException("Arrendatario no encontrado."));

            Publicacion publicacion = publicacionRepository.findById(idPublicacion)
                    .orElseThrow(() -> new IllegalArgumentException("Publicación no encontrada."));

            Vehiculo vehiculo = publicacion.getVehiculo();

            // Obtener la fecha actual y las fechas límite
            LocalDate today = LocalDate.now();
            LocalDate mañana = today.plusDays(1);

            // Validar fechas de inicio y fin
            if (fechaInicio.isBefore(mañana)) {
                return ResponseEntity.badRequest().body("Debes realizar el arriendo con un día de anticipación.");
            }
            if (fechaFinal.isBefore(fechaInicio)) {
                return ResponseEntity.badRequest().body("La fecha de inicio debe ser anterior a la fecha final del arriendo.");
            }

            // Validar el rango de días de arrendamiento
            long dias = fechaFinal.toEpochDay() - fechaInicio.toEpochDay();
            if (dias > 30) {
                return ResponseEntity.badRequest().body("El arriendo no puede superar los 30 días.");
            }

            // Verificar disponibilidad del vehículo
            if (!verificarDisponibilidad(vehiculo, fechaInicio, fechaFinal)) {
                return ResponseEntity.badRequest().body("El vehículo no está disponible en las fechas seleccionadas.");
            }

            // Calcular el precio total y guardar el arriendo
            int precioTotal = (int) (dias * publicacion.getPrecioNormal());
            if(dias == 0){
                precioTotal = publicacion.getPrecioNormal();
            }
            Arriendo arriendo = new Arriendo();
            arriendo.setCancelado(true);
            arriendo.setConformidad(false);
            arriendo.setPrecio(precioTotal);
            arriendo.setFechaInicio(fechaInicio);
            arriendo.setFechaFin(fechaFinal);
            arriendo.setArrendatario(arrendatario);
            arriendo.setVehiculo(vehiculo);

            arriendoRepository.save(arriendo);

            // Crear la boleta
            Boleta nuevaBoleta = new Boleta();
            nuevaBoleta.setEstado("Pagado");
            nuevaBoleta.setFechaPago(LocalDateTime.now().withNano(0));
            nuevaBoleta.setMontoTotal(precioTotal);
            arriendo.setBoleta(nuevaBoleta);
            boletaRepository.save(nuevaBoleta);

            // Cambiar el estado del vehículo
            vehiculo.setDevuelto(false);
            vehiculoRepository.save(vehiculo);

            return ResponseEntity.ok(arriendo); // Respuesta exitosa con el arriendo

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Devuelve el mensaje de error específico
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el arriendo: probablemente un ID duplicado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar el arriendo: " + e.getMessage());
        }
    }


}
