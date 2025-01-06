package com.easywheels.Controller;

import com.easywheels.Model.SolicitudCancelacion;
import com.easywheels.Service.ArriendoService;
import com.easywheels.Service.SolicitudCancelacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/arriendos")
@CrossOrigin
public class ArriendoController {

    @Autowired
    private ArriendoService arriendoService;

    @Autowired
    private SolicitudCancelacionService solicitudCancelacionService;  // Inyección del servicio

    // Endpoint para cancelar un arriendo
    @PutMapping("/cancelar/{id}")
    public ResponseEntity<Void> cancelarArriendo(@PathVariable int id) {
        try {
            // Intentamos cancelar el arriendo
            arriendoService.cancelarArriendo(id);

            // Buscamos la solicitud de cancelación asociada al arriendo
            SolicitudCancelacion solicitudCancelacion = solicitudCancelacionService.buscarSolicitudPorArriendo(id);

            if (solicitudCancelacion != null) {
                // Actualizamos la solicitud de cancelación para marcarla como realizada
                solicitudCancelacionService.actualizarCancelacionRealizada(solicitudCancelacion.getId(), solicitudCancelacion);
            }

            return ResponseEntity.ok().build(); // Respuesta exitosa
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // Si el arriendo no existe
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Error general
        }
    }

}
