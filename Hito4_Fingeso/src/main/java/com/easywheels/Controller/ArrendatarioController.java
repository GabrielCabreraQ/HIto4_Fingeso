package com.easywheels.Controller;

import com.easywheels.Model.Arrendatario;
import com.easywheels.Model.Arriendo;
import com.easywheels.Service.ArrendatarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;


@RestController
@RequestMapping("/arrendatarios")
@CrossOrigin
public class ArrendatarioController {

    @Autowired
    private ArrendatarioService arrendatarioService;

    // Registrar usuario
    @PostMapping("/register")
    public ResponseEntity<Arrendatario> register(@RequestBody Arrendatario nuevo) {
        try {
            Arrendatario registrado = arrendatarioService.register(
                    nuevo.getNombreUsuario(),
                    nuevo.getCorreoUsuario(),
                    nuevo.getContraseniaUsuario(),
                    nuevo.getTelefonoUsuario(),
                    nuevo.getFechaNacimiento(),
                    nuevo.getTipoLicencia(),
                    nuevo.getDocumentos()
            );
            return new ResponseEntity<>(registrado, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Arrendar un vehículo
    @PostMapping("/{idArrendatario}/arrendar")
    public ResponseEntity<?> arrendarVehiculo(
            @PathVariable long idArrendatario,
            @RequestBody Map<String, Object> body) {

        try {
            // Parsear fechas desde el cuerpo de la solicitud
            String fechaInicioStr = (String) body.get("fechaInicio");
            String fechaFinalStr = (String) body.get("fechaFinal");

            // Validar que las fechas no sean nulas
            if (fechaInicioStr == null || fechaFinalStr == null) {
                return ResponseEntity.badRequest().body("Las fechas de inicio y fin son obligatorias.");
            }

            LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
            LocalDate fechaFinal = LocalDate.parse(fechaFinalStr);
            long idPublicacion = ((Number) body.get("idPublicacion")).longValue();

            // Llamar al servicio para arrendar el vehículo
            ResponseEntity<?> response = arrendatarioService.arrendarVehiculo(idArrendatario, idPublicacion, fechaInicio, fechaFinal);
            return response;

        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Formato de fecha no válido. Asegúrate de usar el formato correcto.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + e.getMessage());
        }
    }
}
