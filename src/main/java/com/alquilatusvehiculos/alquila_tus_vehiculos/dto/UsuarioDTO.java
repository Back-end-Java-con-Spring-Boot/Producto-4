package com.alquilatusvehiculos.alquila_tus_vehiculos.dto;

import lombok.AllArgsConstructor;
import com.alquilatusvehiculos.alquila_tus_vehiculos.model.RolUsuario;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long id;
    private String username;
    private RolUsuario rol;
    private Boolean activo;
    private LocalDateTime fechaRegistro;
    private Long clienteId;
}