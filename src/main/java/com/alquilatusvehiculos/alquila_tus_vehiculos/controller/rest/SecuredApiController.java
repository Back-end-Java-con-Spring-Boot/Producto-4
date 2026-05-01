package com.alquilatusvehiculos.alquila_tus_vehiculos.controller.rest;

import com.alquilatusvehiculos.alquila_tus_vehiculos.dto.AlquilerDTO;
import com.alquilatusvehiculos.alquila_tus_vehiculos.service.AlquilerService;
import com.alquilatusvehiculos.alquila_tus_vehiculos.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SecuredApiController {

    @Autowired
    private AlquilerService alquilerService;
    @Autowired
    private ClienteService clienteService;

    /**
     * ENDPOINT SECURIZADO: Crear un nuevo alquiler
     * Método: POST
     * Ruta: /api/alquileres
     */
    @PostMapping("/alquileres")
    public ResponseEntity<AlquilerDTO> crearAlquiler(
            @RequestBody AlquilerDTO alquilerDTO, Authentication authentication
    ) {
        String username = authentication.getName();

        Long clienteIdReal = clienteService.obtenerIdClientePorUsername(username);

        alquilerDTO.setClienteId(clienteIdReal);

        AlquilerDTO nuevoAlquiler = alquilerService.crearAlquilerDesdeApi(alquilerDTO);

        return new ResponseEntity<>(nuevoAlquiler, HttpStatus.CREATED);
    }

    /**
     * ENDPOINT 2 SECURIZADO: Historial de alquileres de un cliente
     * Método: GET
     * Ruta: /api/clientes/{id}/alquileres
     */
    @GetMapping("/clientes/{id}/alquileres")
    public ResponseEntity<?> obtenerAlquileresCliente(
            @PathVariable Long id, Authentication authentication
    ) {
        String username = authentication.getName();

        Long clienteIdReal = clienteService.obtenerIdClientePorUsername(username);

        if (!id.equals(clienteIdReal)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("{ \"error\": \"Acceso denegado\", \"message\": \"No tienes permiso para ver el historial de otro cliente.\" }");
        }

        List<AlquilerDTO> historial = alquilerService.obtenerAlquileresDeClienteParaApi(id);

        return ResponseEntity.ok(historial);
    }
}