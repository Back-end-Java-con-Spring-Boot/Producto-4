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
@Tag(name = "Alquileres API", description = "Gestión de alquileres y reservas")
public class AlquilerApiController {

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


    @GetMapping("/clientes/{id}/alquileres")
    @Operation(summary = "Obtener historial de reservas", description = "Devuelve la lista de alquileres de un cliente específico. El sistema bloquea la petición si el ID de la URL no coincide con el del Token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial recuperado exitosamente"),
            @ApiResponse(responseCode = "401", description = "Token ausente o inválido"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado (Intento de espiar el historial de otro cliente)")
    })
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