package com.alquilatusvehiculos.alquila_tus_vehiculos.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String username;
    private String password;
}