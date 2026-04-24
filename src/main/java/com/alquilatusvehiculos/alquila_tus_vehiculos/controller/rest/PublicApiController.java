package com.alquilatusvehiculos.alquila_tus_vehiculos.controller.rest;

import com.alquilatusvehiculos.alquila_tus_vehiculos.dto.VehiculoDTO;
import com.alquilatusvehiculos.alquila_tus_vehiculos.dto.SucursalDTO;
import com.alquilatusvehiculos.alquila_tus_vehiculos.service.VehiculoService;
import com.alquilatusvehiculos.alquila_tus_vehiculos.service.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PublicApiController {

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private SucursalService sucursalService;


    @GetMapping("/vehiculos")
    public List<VehiculoDTO> getVehiculos() {

        return vehiculoService.obtenerTodosParaApi();
    }

    @GetMapping("/sucursales")
    public List<SucursalDTO> getSucursales() {

        return sucursalService.obtenerTodasParaApi();
    }
}