package com.alquilatusvehiculos.alquila_tus_vehiculos.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sucursal")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String direccion;

    @Column(nullable = false, unique = true, length = 20)
    private String telefono;

    @Column(nullable = false, length = 50)
    private String ciudad;

}