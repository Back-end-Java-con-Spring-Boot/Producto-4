package com.alquilatusvehiculos.alquila_tus_vehiculos.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.alquilatusvehiculos.alquila_tus_vehiculos.model.Usuario;
import com.alquilatusvehiculos.alquila_tus_vehiculos.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alquilatusvehiculos.alquila_tus_vehiculos.model.Alquiler;
import com.alquilatusvehiculos.alquila_tus_vehiculos.model.EstadoAlquiler;
import com.alquilatusvehiculos.alquila_tus_vehiculos.model.Vehiculo;
import com.alquilatusvehiculos.alquila_tus_vehiculos.service.AlquilerService;
import com.alquilatusvehiculos.alquila_tus_vehiculos.service.ClienteService;
import com.alquilatusvehiculos.alquila_tus_vehiculos.service.SucursalService;
import com.alquilatusvehiculos.alquila_tus_vehiculos.service.VehiculoService;

@Controller
public class AlquilerController {

    private final AlquilerService alquilerService;
    private final ClienteService clienteService;
    private final VehiculoService vehiculoService;
    private final SucursalService sucursalService;
    private final UsuarioRepository usuarioRepository;

    public AlquilerController(AlquilerService alquilerService, SucursalService sucursalService,
                              ClienteService clienteService, VehiculoService vehiculoService, UsuarioRepository usuarioRepository) {
        this.alquilerService  = alquilerService;
        this.clienteService   = clienteService;
        this.vehiculoService  = vehiculoService;
        this.sucursalService  = sucursalService;
        this.usuarioRepository = usuarioRepository;
    }

    // ══════════════════════════════════════════════════════════════
    // RUTAS /admin — solo ADMIN
    // ══════════════════════════════════════════════════════════════

    @GetMapping("/admin/alquiler")
    public String listarTodo(Model model) {
        model.addAttribute("alquiler", alquilerService.listarTodosAlquileres());
        return "alquiler/lista";
    }

    @GetMapping("/admin/alquiler/crear")
    public String formularioAlquiler(@ModelAttribute Alquiler alquiler,
                                     @RequestParam(required = false) Long vehiculoId,
                                     Model model) {
        if (alquiler.getVehiculos() == null) {
            alquiler.setVehiculos(new ArrayList<>());
        }

        if (vehiculoId != null) {
            try {
                Vehiculo vehiculoSeleccionado = vehiculoService.findById(vehiculoId);
                alquiler.getVehiculos().add(vehiculoSeleccionado);
                if (alquiler.getSucursal() == null || alquiler.getSucursal().getId() == null) {
                    alquiler.setSucursal(vehiculoSeleccionado.getSucursal());
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Vehículo no encontrado: " + e.getMessage());
            }
        }

        model.addAttribute("alquiler", alquiler);
        model.addAttribute("listaClientes", clienteService.findAll());
        model.addAttribute("listaVehiculos", vehiculoService.findAll());
        model.addAttribute("listaSucursales", sucursalService.obtenerTodas());
        model.addAttribute("fechaMinima", LocalDateTime.now());
        return "alquiler/formulario";
    }

    @PostMapping("/admin/alquiler/guardar")
    public String guardar(@ModelAttribute Alquiler alquiler, Model model) {
        try {
            alquiler.setFechaInicio(
                    LocalDateTime.of(alquiler.getFechaInicioDate(), alquiler.getFechaInicioTime()));
            alquiler.setFechaFin(
                    LocalDateTime.of(alquiler.getFechaFinDate(), alquiler.getFechaFinTime()));

            if (alquiler.getId() != null) {
                alquilerService.actualizarAlquiler(alquiler.getId(), alquiler);
            } else {
                alquilerService.crearAlquiler(alquiler);
            }
            return "redirect:/admin/alquiler";

        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("alquiler", alquiler);
            model.addAttribute("listaClientes", clienteService.findAll());
            model.addAttribute("listaVehiculos", vehiculoService.findAll());
            model.addAttribute("listaSucursales", sucursalService.obtenerTodas());
            return "alquiler/formulario";
        }
    }

    @GetMapping("/admin/alquiler/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        try {
            Alquiler alquiler = alquilerService.buscarPorId(id);

            if (alquiler.getEstado() != EstadoAlquiler.ACTIVO) {
                model.addAttribute("errorMessage", "No se puede editar una reserva finalizada o cancelada");
                model.addAttribute("alquiler", alquilerService.listarTodosAlquileres());
                return "alquiler/lista";
            }

            model.addAttribute("alquiler", alquiler);
            model.addAttribute("listaClientes", clienteService.findAll());
            model.addAttribute("listaVehiculos", vehiculoService.findAll());
            model.addAttribute("listaSucursales", sucursalService.obtenerTodas());
            return "alquiler/formulario";

        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("alquiler", alquilerService.listarTodosAlquileres());
            return "alquiler/lista";
        }
    }

    @GetMapping("/admin/alquiler/cancelar/{id}")
    public String cancelar(@PathVariable Long id, Model model) {
        try {
            alquilerService.cancelarAlquiler(id);
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("alquiler", alquilerService.listarTodosAlquileres());
            return "alquiler/lista";
        }
        return "redirect:/admin/alquiler";
    }

    // ══════════════════════════════════════════════════════════════
    // RUTAS /user — USER + ADMIN (ver alquileres propios)
    // ══════════════════════════════════════════════════════════════

    @GetMapping("/user/reservar")
    public String reservarUser(@ModelAttribute Alquiler alquiler,
                               @RequestParam(required = false) Long vehiculoId,
                               Model model) {
        if (alquiler.getVehiculos() == null) {
            alquiler.setVehiculos(new ArrayList<>());
        }
        if (vehiculoId != null) {
            try {
                Vehiculo v = vehiculoService.findById(vehiculoId);
                alquiler.getVehiculos().add(v);
                if (alquiler.getSucursal() == null || alquiler.getSucursal().getId() == null) {
                    alquiler.setSucursal(v.getSucursal());
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Vehículo no encontrado: " + e.getMessage());
            }
        }
        model.addAttribute("alquiler", alquiler);
        model.addAttribute("listaVehiculos", vehiculoService.findAll());
        model.addAttribute("listaSucursales", sucursalService.obtenerTodas());
        model.addAttribute("fechaMinima", LocalDateTime.now());
        return "alquiler/formulario";
    }

    @PostMapping("/user/reservar/guardar")
    public String guardarUser(@ModelAttribute Alquiler alquiler, Model model, Principal principal) {
        try {
            alquiler.setFechaInicio(
                    LocalDateTime.of(alquiler.getFechaInicioDate(), alquiler.getFechaInicioTime()));
            alquiler.setFechaFin(
                    LocalDateTime.of(alquiler.getFechaFinDate(), alquiler.getFechaFinTime()));

            String usernameLogueado = principal.getName();
            Usuario usuario = usuarioRepository.findByUsername(usernameLogueado)
                            .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

            alquiler.setCliente(usuario.getCliente());

            alquilerService.crearAlquiler(alquiler);
            return "redirect:/user/mis-reservas";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("alquiler", alquiler);
            model.addAttribute("listaClientes", clienteService.findAll());
            model.addAttribute("listaVehiculos", vehiculoService.findAll());
            model.addAttribute("listaSucursales", sucursalService.obtenerTodas());
            return "alquiler/formulario";
        }
    }

    @GetMapping("user/mis-reservas")
    public String verMisReservas(Model model, Principal principal){

        String usernameLogueado = principal.getName();
        Usuario usuario = usuarioRepository.findByUsername(usernameLogueado).orElseThrow();
        Long idClienteLogueado = usuario.getCliente().getId();

        List<Alquiler> misAlquileres = alquilerService.listarTodosAlquileres()
                .stream()
                .filter(a -> a.getCliente().getId().equals(idClienteLogueado))
                .collect(Collectors.toList());

        model.addAttribute("misAlquileres", misAlquileres);
        return "alquiler/mis-reservas";

    }
}