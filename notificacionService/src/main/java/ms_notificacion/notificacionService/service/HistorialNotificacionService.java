package ms_notificacion.notificacionservice.service;

import ms_notificacion.notificacionservice.model.HistorialNotificacion;

import java.util.List;

public interface HistorialNotificacionService {
    List<HistorialNotificacion> listarHistorialNotificacion();
    HistorialNotificacion buscarHistorialNotificacionPorId(Long id);
    HistorialNotificacion crearHistorialNotificacion(HistorialNotificacion historialNotificacion);
    HistorialNotificacion actualizarHistorialNotificacion(HistorialNotificacion historialNotificacion, Long id);
    void eliminarHistorialNotificacion(Long id);
}
