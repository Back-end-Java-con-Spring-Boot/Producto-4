package com.alquilatusvehiculos.alquila_tus_vehiculos.model;

import jakarta.persistence.*;
import jakarta.websocket.ClientEndpoint;

import java.time.LocalDateTime;

@Entity
@Table(name = "alquileres")
public class Alquiler {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private Cliente cliente_id;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private  Sucursal sucursal_id;

    private float precio_total;
    private LocalDateTime fecha_inicio;
    private  LocalDateTime fecha_fin;

    @Enumerated(EnumType.STRING)
    private EstadoAlquiler estado = EstadoAlquiler.ACTIVO;



}
