package ms_notificacion.notificacionservice.controller;


import ms_notificacion.notificacionservice.model.HistorialNotificacion;
import ms_notificacion.notificacionservice.service.HistorialNotificacionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historial-notificaciones")
public class HistorialNotificacionController {
    private final HistorialNotificacionService service;

    public HistorialNotificacionController(HistorialNotificacionService service) {
        this.service = service;
    }

    @GetMapping
    public List<HistorialNotificacion> buscarHistorialNotificacion() {
        return service.listarHistorialNotificacion();
    }

    @GetMapping("/{id}")
    public HistorialNotificacion buscarHistorialNotificacionPorId(@PathVariable Long id) {
        return service.buscarHistorialNotificacionPorId(id);
    }

    @PostMapping
    public HistorialNotificacion crearHistorialNotificacion(@RequestBody HistorialNotificacion historialNotificacion) {
        return service.crearHistorialNotificacion(historialNotificacion);
    }

    @PutMapping("/{id}")
    public HistorialNotificacion actualizarHistorialNotificacion(@PathVariable Long id, @RequestBody HistorialNotificacion historialNotificacion) {
        return service.actualizarHistorialNotificacion(historialNotificacion, id);
    }

    @DeleteMapping("/{id}")
    public void eliminarHistorialNotificacion(@PathVariable Long id) {
        service.eliminarHistorialNotificacion(id);
    }
}
