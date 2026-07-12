package ms_notificacion.notificacionservice.repository;

import ms_notificacion.notificacionservice.model.HistorialNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialNotificacionRepository extends JpaRepository<HistorialNotificacion,Long> {
    List<HistorialNotificacion> findByUsuarioDestinatarioOrderByFechaEnvioDesc(Long usuarioDestinatario);
    long countByUsuarioDestinatarioAndLeidaFalse(Long usuarioDestinatario);
    List<HistorialNotificacion> findByUsuarioDestinatarioAndLeidaFalse(Long usuarioDestinatario);
}
