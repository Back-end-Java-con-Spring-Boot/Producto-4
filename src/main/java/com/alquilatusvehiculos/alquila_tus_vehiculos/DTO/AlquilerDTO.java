package com.alquilatusvehiculos.alquila_tus_vehiculos.DTO;

import com.alquilatusvehiculos.alquila_tus_vehiculos.model.EstadoAlquiler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlquilerDTO {

    private Long id;
    private Long clienteId;
    private Long sucursalId;
    private List<Long> vehiculoIds;
    private BigDecimal precioTotal;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private EstadoAlquiler estado;
}