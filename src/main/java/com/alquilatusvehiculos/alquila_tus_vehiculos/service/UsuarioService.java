package com.alquilatusvehiculos.alquila_tus_vehiculos.service;

import com.alquilatusvehiculos.alquila_tus_vehiculos.model.Cliente;
import com.alquilatusvehiculos.alquila_tus_vehiculos.model.RolUsuario;
import com.alquilatusvehiculos.alquila_tus_vehiculos.model.Usuario;
import com.alquilatusvehiculos.alquila_tus_vehiculos.model.UsuarioRegistroDTO;
import com.alquilatusvehiculos.alquila_tus_vehiculos.repository.ClienteRepository;
import com.alquilatusvehiculos.alquila_tus_vehiculos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void registrar(UsuarioRegistroDTO dto) {

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(RolUsuario.USER);
        usuario.setActivo(true);
        usuarioRepository.save(usuario);

        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setApellidos(dto.getApellidos());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setUsuario(usuario);
        clienteRepository.save(cliente);

    }

    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }
}