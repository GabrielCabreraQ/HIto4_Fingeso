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

    public SolicitudCancelacion actualizarSolicitud(long id, SolicitudCancelacion nuevaSolicitud) {
        return repository.findById(id).map(solicitud -> {
            solicitud.setDescripcion(nuevaSolicitud.getDescripcion());
            solicitud.setCancelacionRealizada(nuevaSolicitud.getCancelacionRealizada());
            return repository.save(solicitud);
        }).orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
    }

    public SolicitudCancelacion actualizarCancelacionRealizada(long id, SolicitudCancelacion nuevaSolicitud) {
        // Buscar la solicitud existente por ID
        return repository.findById(id).map(solicitud -> {
            // Establecer la cancelación como realizada
            solicitud.setCancelacionRealizada(true);
            // Actualizar los demás campos si es necesario
            if (nuevaSolicitud.getDescripcion() != null) {
                solicitud.setDescripcion(nuevaSolicitud.getDescripcion());
            }
            // Guardar la solicitud actualizada
            return repository.save(solicitud);
        }).orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
    }

    public void eliminarSolicitud(long id) {
        repository.deleteById(id);
    }

    // Método para buscar la solicitud por el ID del arriendo
    public SolicitudCancelacion buscarSolicitudPorArriendo(int idArriendo) {
        return repository.findByIdArriendo(idArriendo);  // Asumiendo que el repositorio tiene este método implementado
    }
}
