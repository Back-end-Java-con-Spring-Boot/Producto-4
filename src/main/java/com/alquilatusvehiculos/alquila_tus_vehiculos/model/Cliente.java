package com.alquilatusvehiculos.alquila_tus_vehiculos.model;

import jakarta.persistence.*;
<<<<<<< HEAD
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "clientes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
=======
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
>>>>>>> 137f4393ba3e9d28fc3f72d4029aedd79f2f1fac
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

<<<<<<< HEAD
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre no puede superar 50 caracteres")
    @Column(nullable = false, length = 50)
    private String nombre;

    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    @Size(max = 70, message = "Los apellidos no pueden superar 70 caracteres")
    @Column(nullable = false, length = 70)
    private String apellidos;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email no tiene un formato válido")
    @Size(max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @ToString.Exclude
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Alquiler> alquileres;
=======
    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 70)
    private String apellidos;

    @Column(nullable = false, length = 100, unique = true)
    private String email;
>>>>>>> 137f4393ba3e9d28fc3f72d4029aedd79f2f1fac
}