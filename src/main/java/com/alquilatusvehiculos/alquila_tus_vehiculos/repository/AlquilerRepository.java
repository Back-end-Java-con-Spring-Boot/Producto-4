package com.alquilatusvehiculos.alquila_tus_vehiculos.repository;

import com.alquilatusvehiculos.alquila_tus_vehiculos.model.Alquiler;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlquilerRepository extends JpaRepository<Alquiler, Long> {
}
