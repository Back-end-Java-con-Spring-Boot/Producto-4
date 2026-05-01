package com.alquilatusvehiculos.alquila_tus_vehiculos.controller.rest;

import com.alquilatusvehiculos.alquila_tus_vehiculos.dto.ClienteDTO;
import com.alquilatusvehiculos.alquila_tus_vehiculos.model.Cliente;
import com.alquilatusvehiculos.alquila_tus_vehiculos.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ClienteApiController {

    private final ClienteRepository clienteRepository;

    @GetMapping("/clientes/{id}")
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