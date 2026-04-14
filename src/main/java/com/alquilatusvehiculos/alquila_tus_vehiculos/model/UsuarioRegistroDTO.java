package com.alquilatusvehiculos.alquila_tus_vehiculos.model;

import lombok.Data;

@Data
public class UsuarioRegistroDTO {
    private String nombre;
    private String apellidos;
    private String username;
    private String email;
    private String password;
    private String confirmarPassword;
    private String telefono;
}
