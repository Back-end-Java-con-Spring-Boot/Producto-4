package com.alquilatusvehiculos.alquila_tus_vehiculos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alquilatusvehiculos.alquila_tus_vehiculos.model.Cliente;
import com.alquilatusvehiculos.alquila_tus_vehiculos.service.ClienteService;

import jakarta.validation.Valid;

@Controller
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // ══════════════════════════════════════════════════════════════
    // RUTAS /admin — solo ADMIN
    // ══════════════════════════════════════════════════════════════

    // READ - Listar todos (ADMIN)
    @GetMapping("/admin/clientes")
    public String listar(Model model) {
        model.addAttribute("listaClientes", clienteService.findAll());
        return "clientes/lista";
    }

    // CREATE - Mostrar formulario nuevo (ADMIN)
    @GetMapping("/admin/clientes/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("titulo", "Nuevo Cliente");
        return "clientes/formulario";
    }

    // CREATE - Guardar nuevo cliente (ADMIN)
    @PostMapping("/admin/clientes/nuevo")
    public String guardarNuevo(@Valid @ModelAttribute("cliente") Cliente cliente,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttrs) {

        if (clienteService.emailYaExiste(cliente.getEmail(), null)) {
            result.rejectValue("email", "error.cliente", "Este email ya está registrado");
        }

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Nuevo Cliente");
            return "clientes/formulario";
        }

        clienteService.save(cliente);
        redirectAttrs.addFlashAttribute("mensajeExito", "Cliente creado correctamente");
        return "redirect:/admin/clientes";
    }

    // UPDATE - Mostrar formulario edición (ADMIN)
    @GetMapping("/admin/clientes/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id,
                                          Model model,
                                          RedirectAttributes redirectAttrs) {
        return clienteService.findById(id).map(cliente -> {
            model.addAttribute("cliente", cliente);
            model.addAttribute("titulo", "Editar Cliente");
            return "clientes/formulario";
        }).orElseGet(() -> {
            redirectAttrs.addFlashAttribute("mensajeError", "Cliente no encontrado");
            return "redirect:/admin/clientes";
        });
    }

    // UPDATE - Guardar cambios (ADMIN)
    @PostMapping("/admin/clientes/editar/{id}")
    public String guardarEdicion(@PathVariable Long id,
                                 @Valid @ModelAttribute("cliente") Cliente cliente,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttrs) {

        if (clienteService.emailYaExiste(cliente.getEmail(), id)) {
            result.rejectValue("email", "error.cliente", "Este email ya está registrado");
        }

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Editar Cliente");
            return "clientes/formulario";
        }

        cliente.setId(id);
        clienteService.save(cliente);
        redirectAttrs.addFlashAttribute("mensajeExito", "Cliente actualizado correctamente");
        return "redirect:/admin/clientes";
    }

    // DELETE (ADMIN)
    @PostMapping("/admin/clientes/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        clienteService.deleteById(id);
        redirectAttrs.addFlashAttribute("mensajeExito", "Cliente eliminado correctamente");
        return "redirect:/admin/clientes";
    }

    // ══════════════════════════════════════════════════════════════
    // RUTAS /user — ADMIN y USER
    // ══════════════════════════════════════════════════════════════

    // READ - Ver detalle de un cliente (USER + ADMIN)
    @GetMapping("/user/clientes/{id}")
    public String verDetalle(@PathVariable Long id,
                             Model model,
                             RedirectAttributes redirectAttrs) {
        return clienteService.findById(id).map(cliente -> {
            model.addAttribute("cliente", cliente);
            return "clientes/detalle";
        }).orElseGet(() -> {
            redirectAttrs.addFlashAttribute("mensajeError", "Cliente no encontrado");
            return "redirect:/admin/clientes";
        });
    }
}