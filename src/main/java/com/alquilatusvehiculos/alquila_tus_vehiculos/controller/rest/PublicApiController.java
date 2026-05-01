package com.alquilatusvehiculos.alquila_tus_vehiculos.controller.rest;

import com.alquilatusvehiculos.alquila_tus_vehiculos.dto.VehiculoDTO;
import com.alquilatusvehiculos.alquila_tus_vehiculos.dto.SucursalDTO;
import com.alquilatusvehiculos.alquila_tus_vehiculos.service.VehiculoService;
import com.alquilatusvehiculos.alquila_tus_vehiculos.service.SucursalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Catálogo Público API", description = "Endpoints públicos para consultar los vehículos y sucursales disponibles. No requieren autenticación.")
public class PublicApiController {

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private SucursalService sucursalService;


    @GetMapping("/vehiculos")
    @Operation(summary = "Listar todos los vehículos", description = "Devuelve el catálogo completo de vehículos disponibles en la flota para mostrarlos en el frontend.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vehículos recuperada con éxito")
    })
    public List<VehiculoDTO> getVehiculos() {

        return vehiculoService.obtenerTodosParaApi();
    }

    @GetMapping("/sucursales")
    @Operation(summary = "Listar todas las sucursales", description = "Devuelve la lista de las diferentes oficinas de Sleipnir donde se pueden recoger o entregar los vehículos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de sucursales recuperada con éxito")
    })
    public List<SucursalDTO> getSucursales() {

        return sucursalService.obtenerTodasParaApi();
    }
}