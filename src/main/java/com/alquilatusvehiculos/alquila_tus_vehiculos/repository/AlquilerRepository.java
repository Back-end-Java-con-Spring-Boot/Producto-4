package com.alquilatusvehiculos.alquila_tus_vehiculos.repository;

import com.alquilatusvehiculos.alquila_tus_vehiculos.model.Alquiler;
import com.alquilatusvehiculos.alquila_tus_vehiculos.model.EstadoAlquiler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AlquilerRepository extends JpaRepository<Alquiler, Long> {
    List<Alquiler> findByEstado(EstadoAlquiler estado);

    List<Alquiler> findByFechaInicioBetween(LocalDateTime inicio, LocalDateTime fin);

    List<Alquiler> findByClienteEmail(String email_cliente);

    //Lo usará vehiculos para no eliminar un vehículo que tenga un alquiler con estado ACTIVO
    //@Query("SELECT COUNT(a) > 0 FROM Alquiler a JOIN a.vehiculos v WHERE v.id = :vehiculoId AND a.estado = :estado")
    //boolean comprobarSiVehiculoEstaEnUso(@Param("vehiculoId") Long vehiculoId, @Param("estado") EstadoAlquiler estado);
    // probar si funciona esta versión simplificada:
    //boolean existsByVehiculos_IdAndEstado(Long vehiculoId, EstadoAlquiler estado);

}
