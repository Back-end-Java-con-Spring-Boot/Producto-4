package com.alquilatusvehiculos.alquila_tus_vehiculos.repository;

import com.alquilatusvehiculos.alquila_tus_vehiculos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}
