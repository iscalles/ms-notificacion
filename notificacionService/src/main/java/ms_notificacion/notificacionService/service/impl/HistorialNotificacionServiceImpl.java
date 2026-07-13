package ms_notificacion.notificacionservice.service.impl;

import ms_notificacion.notificacionservice.client.UsuarioClient;
import ms_notificacion.notificacionservice.dto.NotificacionEventoDTO;
import ms_notificacion.notificacionservice.model.HistorialNotificacion;
import ms_notificacion.notificacionservice.repository.HistorialNotificacionRepository;
import ms_notificacion.notificacionservice.service.HistorialNotificacionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HistorialNotificacionServiceImpl implements HistorialNotificacionService {
    private final HistorialNotificacionRepository historialNotificacionRepository;
    private final UsuarioClient usuarioClient;

    public HistorialNotificacionServiceImpl(HistorialNotificacionRepository historialNotificacionRepository,
                                             UsuarioClient usuarioClient) {
        this.historialNotificacionRepository = historialNotificacionRepository;
        this.usuarioClient = usuarioClient;
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
    public void eliminarHistorialNotificacion(Long id) {
        HistorialNotificacion existente = historialNotificacionRepository.findById(id).orElse(null);
        if (existente != null) {
            historialNotificacionRepository.delete(existente);
        } else {
            throw new RuntimeException("Notificación no encontrada con id: " + id);
        }
    }

    @Override
    public List<HistorialNotificacion> crearEvento(NotificacionEventoDTO evento) {
        List<Long> destinatarios = new ArrayList<>();

        if (evento.isNotificarEstudiante() && evento.getEstudianteIdUsuario() != null) {
            destinatarios.add(evento.getEstudianteIdUsuario());
        }

        if (evento.isNotificarApoderados() && evento.getEstudianteIdUsuario() != null) {
            List<Long> apoderadosIds = usuarioClient.obtenerApoderadosIds(evento.getEstudianteIdUsuario());
            if (apoderadosIds != null) {
                destinatarios.addAll(apoderadosIds);
            }
        }

        LocalDateTime ahora = LocalDateTime.now();
        List<HistorialNotificacion> creadas = new ArrayList<>();
        for (Long idDestinatario : destinatarios) {
            HistorialNotificacion notificacion = new HistorialNotificacion();
            notificacion.setUsuarioDestinatario(idDestinatario);
            notificacion.setTipoNotificacion(evento.getTipoNotificacion());
            notificacion.setIdReferencia(evento.getIdReferencia());
            notificacion.setAsunto(evento.getAsunto());
            notificacion.setMensaje(evento.getMensaje());
            notificacion.setLeida(false);
            notificacion.setFechaEnvio(ahora);
            creadas.add(historialNotificacionRepository.save(notificacion));
        }
        return creadas;
    }

    @Override
    public List<HistorialNotificacion> listarPorUsuario(Long idUsuario) {
        return historialNotificacionRepository.findByUsuarioDestinatarioOrderByFechaEnvioDesc(idUsuario);
    }

    @Override
    public long contarNoLeidas(Long idUsuario) {
        return historialNotificacionRepository.countByUsuarioDestinatarioAndLeidaFalse(idUsuario);
    }

    @Override
    public HistorialNotificacion marcarLeida(Long id) {
        HistorialNotificacion notificacion = historialNotificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con id: " + id));
        notificacion.setLeida(true);
        notificacion.setFechaLectura(LocalDateTime.now());
        return historialNotificacionRepository.save(notificacion);
    }

    @Override
    public void marcarTodasLeidas(Long idUsuario) {
        List<HistorialNotificacion> pendientes = historialNotificacionRepository
                .findByUsuarioDestinatarioAndLeidaFalse(idUsuario);
        LocalDateTime ahora = LocalDateTime.now();
        for (HistorialNotificacion notificacion : pendientes) {
            notificacion.setLeida(true);
            notificacion.setFechaLectura(ahora);
        }
        historialNotificacionRepository.saveAll(pendientes);
    }
}
