package com.alquilatusvehiculos.alquila_tus_vehiculos.controller;

import com.alquilatusvehiculos.alquila_tus_vehiculos.model.UsuarioRegistroDTO;
import com.alquilatusvehiculos.alquila_tus_vehiculos.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro (Model model){
        model.addAttribute("usuarioDto", new UsuarioRegistroDTO());
        return "auth/registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario (@ModelAttribute("usuarioDto") UsuarioRegistroDTO registroDTO, Model model) {
        if (!registroDTO.getPassword().equals(registroDTO.getConfirmarPassword())) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "auth/registro";
        }
        usuarioService.registrar(registroDTO);
        return "redirect:/auth/login?exito";
    }
}