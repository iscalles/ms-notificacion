package ms_notificacion.notificacionservice.controller;


import ms_notificacion.notificacionservice.dto.NotificacionEventoDTO;
import ms_notificacion.notificacionservice.model.HistorialNotificacion;
import ms_notificacion.notificacionservice.service.HistorialNotificacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificaciones")
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

    @DeleteMapping("/{id}")
    public void eliminarHistorialNotificacion(@PathVariable Long id) {
        service.eliminarHistorialNotificacion(id);
    }

    @PostMapping("/evento")
    public ResponseEntity<List<HistorialNotificacion>> crearEvento(@RequestBody NotificacionEventoDTO evento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearEvento(evento));
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<HistorialNotificacion> listarPorUsuario(@PathVariable Long idUsuario) {
        return service.listarPorUsuario(idUsuario);
    }

    @GetMapping("/usuario/{idUsuario}/no-leidas/count")
    public long contarNoLeidas(@PathVariable Long idUsuario) {
        return service.contarNoLeidas(idUsuario);
    }

    @PutMapping("/{id}/marcar-leida")
    public HistorialNotificacion marcarLeida(@PathVariable Long id) {
        return service.marcarLeida(id);
    }

    @PutMapping("/usuario/{idUsuario}/marcar-todas-leidas")
    public ResponseEntity<Void> marcarTodasLeidas(@PathVariable Long idUsuario) {
        service.marcarTodasLeidas(idUsuario);
        return ResponseEntity.ok().build();
    }
}
