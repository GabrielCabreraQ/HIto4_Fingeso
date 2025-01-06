package com.easywheels.Service;

import com.easywheels.Model.Arriendo;
import com.easywheels.Repository.ArriendoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArriendoService {

    @Autowired
    private ArriendoRepository arriendoRepository;

    // Método para cancelar el arriendo
    public void cancelarArriendo(int id) {
        Optional<Arriendo> arriendoOpt = arriendoRepository.findById(id);

        if (arriendoOpt.isPresent()) {
            Arriendo arriendo = arriendoOpt.get();
            arriendo.setArriendo_cancelado(true); // Cambiar estado a true
            arriendoRepository.save(arriendo);  // Guardar cambios
        } else {
            throw new IllegalArgumentException("El arriendo con el ID especificado no existe.");
        }
    }
}
