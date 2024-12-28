package com.easywheels.Service;

import com.easywheels.Repository.SolicitudCancelacionRepository;
import com.easywheels.Model.SolicitudCancelacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudCancelacionService {

    @Autowired
    private SolicitudCancelacionRepository repository;

    public SolicitudCancelacion crearSolicitud(SolicitudCancelacion solicitud) {
        solicitud.setCancelacionRealizada(false); // Garantiza que sea falso al crear
        return repository.save(solicitud);
    }

    public List<SolicitudCancelacion> listarSolicitudes() {
        return repository.findAll();
    }

    public SolicitudCancelacion actualizarSolicitud(Integer id, SolicitudCancelacion nuevaSolicitud) {
        return repository.findById(id).map(solicitud -> {
            solicitud.setDescripcion(nuevaSolicitud.getDescripcion());
            solicitud.setCancelacionRealizada(nuevaSolicitud.getCancelacionRealizada());
            return repository.save(solicitud);
        }).orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
    }

    public void eliminarSolicitud(Integer id) {
        repository.deleteById(id);
    }
}
