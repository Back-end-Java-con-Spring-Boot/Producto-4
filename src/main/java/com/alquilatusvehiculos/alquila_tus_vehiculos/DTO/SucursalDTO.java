package com.alquilatusvehiculos.alquila_tus_vehiculos.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SucursalDTO {

    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String ciudad;
}
