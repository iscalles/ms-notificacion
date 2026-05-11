package ms_notificacion.notificacionservice.service.impl;

import ms_notificacion.notificacionservice.model.HistorialNotificacion;
import ms_notificacion.notificacionservice.repository.HistorialNotificacionRepository;
import ms_notificacion.notificacionservice.service.HistorialNotificacionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistorialNotificacionServiceImpl implements HistorialNotificacionService {
    private final HistorialNotificacionRepository historialNotificacionRepository;

    public HistorialNotificacionServiceImpl(HistorialNotificacionRepository historialNotificacionRepository) {
        this.historialNotificacionRepository = historialNotificacionRepository;
    }

    @Override
    public List<HistorialNotificacion> listarHistorialNotificacion() {
        return historialNotificacionRepository.findAll();
    }

    @Override
    public HistorialNotificacion buscarHistorialNotificacionPorId(Long id) {
        return historialNotificacionRepository.findById(id).orElse(null);
    }

    @Override
    public HistorialNotificacion crearHistorialNotificacion(HistorialNotificacion historialNotificacion) {
        return historialNotificacionRepository.save(historialNotificacion);
    }

    @Override
    public HistorialNotificacion actualizarHistorialNotificacion(HistorialNotificacion historialNotificacion, Long id) {
        HistorialNotificacion historialExistente = historialNotificacionRepository.findById(id).orElse(null);
        if (historialExistente != null) {
            historialExistente.setAsunto(historialNotificacion.getAsunto());
            historialExistente.setMensaje(historialNotificacion.getMensaje());
            historialExistente.setFechaEnvio(historialNotificacion.getFechaEnvio());
            historialExistente.setUsuarioDestinatario(historialNotificacion.getUsuarioDestinatario());
            historialExistente.setTipoCanal(historialNotificacion.getTipoCanal());
            historialExistente.setEstadoEnvio(historialNotificacion.getEstadoEnvio());
            return historialNotificacionRepository.save(historialExistente);
        } else {
            throw new RuntimeException("Historial de notificación no encontrado con id: " + id);
        }
    }

    @Override
    public void eliminarHistorialNotificacion(Long id) {
        HistorialNotificacion historialExistente = historialNotificacionRepository.findById(id).orElse(null);
        if (historialExistente != null) {
            historialNotificacionRepository.delete(historialExistente);
        } else {
            throw new RuntimeException("Historial de notificación no encontrado con id: " + id);
        }
    }
}
