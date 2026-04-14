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

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void registrar(UsuarioRegistroDTO dto) {

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(RolUsuario.USER);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setApellidos(dto.getApellidos());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());

        cliente.setUsuario(usuarioGuardado);

        clienteRepository.save(cliente);
    }
}