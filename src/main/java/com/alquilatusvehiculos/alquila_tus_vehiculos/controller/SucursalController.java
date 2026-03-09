package com.alquilatusvehiculos.alquila_tus_vehiculos.controller;

import com.alquilatusvehiculos.alquila_tus_vehiculos.model.Sucursal;
import com.alquilatusvehiculos.alquila_tus_vehiculos.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sucursales")
@RequiredArgsConstructor
public class SucursalController {

    private final SucursalRepository sucursalRepository;

    @GetMapping("/lista")
    public String mostrarSucursales(Model model){
        List<Sucursal> listaSucursal = sucursalRepository.findAll();
        model.addAttribute("listaSucursal", listaSucursal);
        return "sucursales/lista";
    }

    @GetMapping("/nueva")
    public String nuevaSucursal(Model model){
        model.addAttribute("sucursal", new Sucursal());
        return "sucursales/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarFormulario(@PathVariable Long id, Model model) { // Cambiado 'long' por 'Long'
        Sucursal sucursalExistente = sucursalRepository.findById(id).orElse(null);
        model.addAttribute("sucursal", sucursalExistente);
        return "sucursales/formulario";
    }

    @PostMapping("/guardar")
    public String guardarSucursal(@ModelAttribute Sucursal sucursal){
        sucursalRepository.save(sucursal);
        return "redirect:/sucursales/lista";
    }

    @GetMapping("/borrar/{id}")
    public String borrarSucursal(@PathVariable Long id) {
        sucursalRepository.deleteById(id);
        return "redirect:/sucursales/lista";
    }
}