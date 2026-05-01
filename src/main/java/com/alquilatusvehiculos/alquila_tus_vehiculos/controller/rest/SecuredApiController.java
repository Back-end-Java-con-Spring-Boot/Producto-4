package com.alquilatusvehiculos.alquila_tus_vehiculos.controller.rest;

import com.alquilatusvehiculos.alquila_tus_vehiculos.dto.AlquilerDTO;
import com.alquilatusvehiculos.alquila_tus_vehiculos.service.AlquilerService;
import com.alquilatusvehiculos.alquila_tus_vehiculos.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "2. Alquileres Securizados", description = "Operaciones privadas para clientes. Requieren Token JWT.")
public class SecuredApiController {

    @Autowired
    private AlquilerService alquilerService;
    @Autowired
    private ClienteService clienteService;

    @PostMapping("/alquileres")
    @Operation(summary = "Crear una nueva reserva", description = "Crea un alquiler. Por seguridad, ignora el clienteId enviado en el JSON y asigna automáticamente la reserva al dueño del Token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (fechas incorrectas, sin vehículos...)"),
            @ApiResponse(responseCode = "401", description = "Token ausente o inválido")
    })
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
     * ENDPOINT SECURIZADO: Historial de alquileres de un cliente
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