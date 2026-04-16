package com.alquilatusvehiculos.alquila_tus_vehiculos.controller;

import com.alquilatusvehiculos.alquila_tus_vehiculos.model.UsuarioRegistroDTO;
import com.alquilatusvehiculos.alquila_tus_vehiculos.service.UsuarioService;
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
    private final UsuarioService usuarioService;

    public ClienteController(ClienteService clienteService, UsuarioService usuarioService) {
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;
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

    // READ - Ver detalle de un cliente (ADMIN)
    @GetMapping("/admin/clientes/{id}")
    public String verDetalleAdmin(@PathVariable Long id,
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

   // CREATE - Mostrar formulario nuevo (ADMIN)
    @GetMapping("/admin/clientes/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("usuarioDto", new UsuarioRegistroDTO());
        model.addAttribute("titulo", "Nuevo Cliente");
        return "clientes/formulario-nuevo";
    }

    // CREATE - Guardar nuevo cliente (ADMIN)
    @PostMapping("/admin/clientes/nuevo")
    public String guardarNuevo(@Valid @ModelAttribute("usuarioDto") UsuarioRegistroDTO dto,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttrs) {

        if (clienteService.emailYaExiste(dto.getEmail(), null)) {
            result.rejectValue("email", "error.usuarioDto", "Este email ya está registrado");
        }

        if (!dto.getPassword().equals(dto.getConfirmarPassword())){
            result.rejectValue("confirmarPassword","error.usuarioDto","Las contraseñas no coinciden");
        }

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Nuevo Cliente");
            return "clientes/formulario-nuevo";
        }

        usuarioService.registrar(dto);
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
            return "clientes/formulario-editar";
        }).orElseGet(() -> {
            redirectAttrs.addFlashAttribute("mensajeError", "Cliente no encontrado");
            return "redirect:/admin/clientes";
        });
    }

    // UPDATE - Guardar cambios (ADMIN)
    @PostMapping("/admin/clientes/editar/{id}")
    public String guardarEdicion(@PathVariable Long id,
                                 @Valid @ModelAttribute("cliente") Cliente clienteModificado,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttrs) {

        if (clienteService.emailYaExiste(clienteModificado.getEmail(), id)) {
            result.rejectValue("email", "error.cliente", "Este email ya está registrado");
        }

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Editar Cliente");
            return "clientes/formulario-editar";
        }

        Cliente clienteOriginal = clienteService.findById(id).orElseThrow();

        clienteOriginal.setNombre(clienteModificado.getNombre());
        clienteOriginal.setApellidos(clienteModificado.getApellidos());
        clienteOriginal.setEmail(clienteModificado.getEmail());
        clienteOriginal.setTelefono(clienteModificado.getTelefono());

        clienteService.save(clienteOriginal);
        redirectAttrs.addFlashAttribute("mensajeExito", "Cliente actualizado correctamente");
        return "redirect:/admin/clientes";
    }

    // DELETE (ADMIN)
    @PostMapping("/admin/clientes/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttrs) {

        try{
            Cliente cliente = clienteService.findById(id).orElseThrow(()-> new RuntimeException("Cliente no encontrado"));

            if (cliente.getUsuario() != null){
                cliente.getUsuario().setCliente(null);
                Long idUsuario = cliente.getUsuario().getId();
                cliente.setUsuario(null);

                clienteService.deleteById(id);

                usuarioService.deleteById(idUsuario);
            } else{
                clienteService.deleteById(id);
            }
            redirectAttrs.addFlashAttribute("mensajeExito", "Cliente eliminado correctamente");
        }catch (Exception e){
            redirectAttrs.addFlashAttribute("mensajeError", "Error al eliminar el cliente: " + e.getMessage());
        }

        return "redirect:/admin/clientes";
    }

    // ══════════════════════════════════════════════════════════════
    // RUTAS /user — ADMIN y USER
    // ══════════════════════════════════════════════════════════════

    // READ - Ver detalle de un cliente (USER)
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