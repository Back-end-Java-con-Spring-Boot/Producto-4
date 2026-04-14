package com.alquilatusvehiculos.alquila_tus_vehiculos.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alquilatusvehiculos.alquila_tus_vehiculos.model.Vehiculo;
import com.alquilatusvehiculos.alquila_tus_vehiculos.service.SucursalService;
import com.alquilatusvehiculos.alquila_tus_vehiculos.service.VehiculoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;
    private final SucursalService sucursalService;

    // ══════════════════════════════════════════════════════════════
    // RUTAS /admin — solo ADMIN
    // ══════════════════════════════════════════════════════════════

    @GetMapping("/admin/vehiculos")
    public String listarVehiculos(Model model) {
        model.addAttribute("vehiculos", vehiculoService.findAll());
        return "vehiculos/admin";
    }

    @GetMapping("/admin/vehiculos/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        model.addAttribute("listaSucursales", sucursalService.obtenerTodas());
        return "vehiculos/formulario";
    }

    @PostMapping("/admin/vehiculos/nuevo")
    public String guardarVehiculo(Vehiculo nuevoVehiculo, RedirectAttributes redirectAttrs) {
        vehiculoService.save(nuevoVehiculo);
        redirectAttrs.addFlashAttribute("mensajeExito", "Vehículo creado correctamente.");
        return "redirect:/admin/vehiculos";
    }

    @GetMapping("/admin/vehiculos/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model,
                                          RedirectAttributes redirectAttrs) {
        try {
            Vehiculo vehiculo = vehiculoService.findById(id);
            model.addAttribute("vehiculo", vehiculo);
            model.addAttribute("listaSucursales", sucursalService.obtenerTodas());
            return "vehiculos/formulario";
        } catch (IllegalArgumentException e) {
            redirectAttrs.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/admin/vehiculos";
        }
    }

    @PostMapping("/admin/vehiculos/editar/{id}")
    public String editarVehiculo(@PathVariable Long id, Vehiculo vehiculo,
                                  RedirectAttributes redirectAttrs) {
        vehiculo.setId(id);
        vehiculoService.save(vehiculo);
        redirectAttrs.addFlashAttribute("mensajeExito", "Vehículo editado correctamente.");
        return "redirect:/admin/vehiculos";
    }

    @GetMapping("/admin/vehiculos/eliminar/{id}")
    public String eliminarVehiculo(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        try {
            vehiculoService.deleteById(id);
            redirectAttrs.addFlashAttribute("mensajeExito", "Vehículo eliminado correctamente.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            redirectAttrs.addFlashAttribute("mensajeError", e.getMessage());
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("mensajeError",
                    "No se puede eliminar: El vehículo está asociado a un historial de alquileres.");
        }
        return "redirect:/admin/vehiculos";
    }

    // ══════════════════════════════════════════════════════════════
    // API JSON — accesible por USER y ADMIN
    // ══════════════════════════════════════════════════════════════

    @GetMapping("/api/vehiculos/sucursal/{id}")
    @ResponseBody
    public List<Vehiculo> vehiculosPorSucursalJson(@PathVariable Long id) {
        return vehiculoService.findBySucursal(id);
    }
}