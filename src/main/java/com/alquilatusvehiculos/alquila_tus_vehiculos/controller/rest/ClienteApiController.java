package com.alquilatusvehiculos.alquila_tus_vehiculos.controller.rest;

import com.alquilatusvehiculos.alquila_tus_vehiculos.dto.ClienteDTO;
import com.alquilatusvehiculos.alquila_tus_vehiculos.model.Cliente;
import com.alquilatusvehiculos.alquila_tus_vehiculos.repository.ClienteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Clientes API", description = "Gestión de la información de los perfiles de clientes.")
public class ClienteApiController {

    private final ClienteRepository clienteRepository;

    @GetMapping("/clientes/{id}")
    @Operation(summary = "Obtener perfil de cliente", description = "Busca y devuelve los datos públicos de un cliente específico basándose en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado y datos devueltos exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado (Falta Token JWT)"),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado ningún cliente con ese ID")
    })
    public ResponseEntity<?> getClienteById(@PathVariable Long id) {
        return clienteRepository.findById(id)
            .map(cliente -> ResponseEntity.ok(toDTO(cliente)))
            .orElse(ResponseEntity.notFound().build());
    }

    private ClienteDTO toDTO(Cliente cliente) {
        return new ClienteDTO(
            cliente.getId(),
            cliente.getNombre(),
            cliente.getApellidos(),
            cliente.getEmail(),
            cliente.getTelefono(),
            cliente.getUsuario().getId()
        );
    }
}