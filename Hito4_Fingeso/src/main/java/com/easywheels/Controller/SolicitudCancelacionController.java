package com.easywheels.Controller;
import com.easywheels.Model.SolicitudCancelacion;
import com.easywheels.Service.SolicitudCancelacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solicitudes")
@CrossOrigin
public class SolicitudCancelacionController {

    @Autowired
    private SolicitudCancelacionService service;

    @PostMapping
    public SolicitudCancelacion crearSolicitud(@RequestBody SolicitudCancelacion solicitud) {
        return service.crearSolicitud(solicitud);
    }

    @GetMapping
    public List<SolicitudCancelacion> listarSolicitudes() {
        return service.listarSolicitudes();
    }

    @PutMapping("/{id}")
    public SolicitudCancelacion actualizarSolicitud(@PathVariable Integer id, @RequestBody SolicitudCancelacion solicitud) {
        return service.actualizarSolicitud(id, solicitud);
    }

    @DeleteMapping("/{id}")
    public void eliminarSolicitud(@PathVariable Integer id) {
        service.eliminarSolicitud(id);
    }
}
