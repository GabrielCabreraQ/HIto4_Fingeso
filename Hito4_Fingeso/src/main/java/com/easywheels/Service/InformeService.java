package com.easywheels.Service;

import com.easywheels.Model.Informe;
import com.easywheels.Repository.InformeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InformeService {

    @Autowired
    private InformeRepository informeRepository;

    public List<Informe> obtenerTodosLosInformes() {
        return informeRepository.findAll();
    }
}
