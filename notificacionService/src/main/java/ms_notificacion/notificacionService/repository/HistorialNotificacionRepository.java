package ms_notificacion.notificacionservice.repository;

import ms_notificacion.notificacionservice.model.HistorialNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialNotificacionRepository extends JpaRepository<HistorialNotificacion,Long> {
}
