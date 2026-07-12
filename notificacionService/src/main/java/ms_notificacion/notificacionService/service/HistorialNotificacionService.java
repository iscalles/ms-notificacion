package ms_notificacion.notificacionservice.service;

import ms_notificacion.notificacionservice.dto.NotificacionEventoDTO;
import ms_notificacion.notificacionservice.model.HistorialNotificacion;

import java.util.List;

public interface HistorialNotificacionService {
    List<HistorialNotificacion> listarHistorialNotificacion();
    HistorialNotificacion buscarHistorialNotificacionPorId(Long id);
    void eliminarHistorialNotificacion(Long id);

    List<HistorialNotificacion> crearEvento(NotificacionEventoDTO evento);
    List<HistorialNotificacion> listarPorUsuario(Long idUsuario);
    long contarNoLeidas(Long idUsuario);
    HistorialNotificacion marcarLeida(Long id);
    void marcarTodasLeidas(Long idUsuario);
}
