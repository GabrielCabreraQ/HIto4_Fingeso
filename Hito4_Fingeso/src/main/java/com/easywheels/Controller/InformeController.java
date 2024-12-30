package com.easywheels.Controller;

import com.easywheels.Model.Informe;
import com.easywheels.Service.InformeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/informes")
public class InformeController {

    private final InformeService informeService;

    public InformeController(InformeService informeService) {
        this.informeService = informeService;
    }

    // Endpoint para obtener todos los informes
    @GetMapping
    public List<Informe> obtenerTodosLosInformes() {
        return informeService.obtenerTodosLosInformes();
    }
}
