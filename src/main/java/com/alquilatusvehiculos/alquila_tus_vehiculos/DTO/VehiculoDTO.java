package com.alquilatusvehiculos.alquila_tus_vehiculos.DTO;

import com.alquilatusvehiculos.alquila_tus_vehiculos.model.TipoVehiculo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoDTO {

    private Long id;
    private String nombre;
    private String matricula;
    private int plazas;
    private BigDecimal precioDia;
    private TipoVehiculo tipoVehiculo;
    private Long sucursalId;
    private String imagenUrl;
}