package ms_notificacion.notificacionservice;

import ms_notificacion.notificacionservice.client.UsuarioClient;
import ms_notificacion.notificacionservice.dto.NotificacionEventoDTO;
import ms_notificacion.notificacionservice.model.HistorialNotificacion;
import ms_notificacion.notificacionservice.repository.HistorialNotificacionRepository;
import ms_notificacion.notificacionservice.service.impl.HistorialNotificacionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistorialNotificacionServiceImplTest {

    @Mock private HistorialNotificacionRepository historialRepository;
    @Mock private UsuarioClient usuarioClient;

    private HistorialNotificacionServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new HistorialNotificacionServiceImpl(historialRepository, usuarioClient);
    }

    // ── listarHistorialNotificacion ───────────────────────────────────────────

    @Test
    void listar_retornaListaDelRepositorio() {
        List<HistorialNotificacion> lista = List.of(new HistorialNotificacion(), new HistorialNotificacion());
        when(historialRepository.findAll()).thenReturn(lista);
        assertThat(service.listarHistorialNotificacion()).hasSize(2);
    }

    // ── buscarHistorialNotificacionPorId ──────────────────────────────────────

    @Test
    void buscarPorId_existente_retornaNotificacion() {
        HistorialNotificacion n = notificacion(1L, 10L, false);
        when(historialRepository.findById(1L)).thenReturn(Optional.of(n));
        assertThat(service.buscarHistorialNotificacionPorId(1L)).isEqualTo(n);
    }

    @Test
    void buscarPorId_noExiste_retornaNull() {
        when(historialRepository.findById(99L)).thenReturn(Optional.empty());
        assertThat(service.buscarHistorialNotificacionPorId(99L)).isNull();
    }

    // ── eliminarHistorialNotificacion ─────────────────────────────────────────

    @Test
    void eliminar_existente_invocaDelete() {
        HistorialNotificacion n = notificacion(1L, 10L, false);
        when(historialRepository.findById(1L)).thenReturn(Optional.of(n));

        service.eliminarHistorialNotificacion(1L);
        verify(historialRepository).delete(n);
    }

    @Test
    void eliminar_noExiste_lanzaExcepcion() {
        when(historialRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.eliminarHistorialNotificacion(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    // ── marcarLeida ───────────────────────────────────────────────────────────

    @Test
    void marcarLeida_existente_setLeidaTrue() {
        HistorialNotificacion n = notificacion(1L, 10L, false);
        when(historialRepository.findById(1L)).thenReturn(Optional.of(n));
        when(historialRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        HistorialNotificacion resultado = service.marcarLeida(1L);
        assertThat(resultado.isLeida()).isTrue();
        assertThat(resultado.getFechaLectura()).isNotNull();
    }

    @Test
    void marcarLeida_noExiste_lanzaExcepcion() {
        when(historialRepository.findById(5L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.marcarLeida(5L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("5");
    }

    // ── contarNoLeidas ────────────────────────────────────────────────────────

    @Test
    void contarNoLeidas_retornaConteoDelRepositorio() {
        when(historialRepository.countByUsuarioDestinatarioAndLeidaFalse(10L)).thenReturn(3L);
        assertThat(service.contarNoLeidas(10L)).isEqualTo(3L);
    }

    // ── marcarTodasLeidas ─────────────────────────────────────────────────────

    @Test
    void marcarTodasLeidas_setLeidaEnCadaPendiente() {
        HistorialNotificacion n1 = notificacion(1L, 10L, false);
        HistorialNotificacion n2 = notificacion(2L, 10L, false);
        when(historialRepository.findByUsuarioDestinatarioAndLeidaFalse(10L)).thenReturn(List.of(n1, n2));

        service.marcarTodasLeidas(10L);

        assertThat(n1.isLeida()).isTrue();
        assertThat(n2.isLeida()).isTrue();
        verify(historialRepository).saveAll(List.of(n1, n2));
    }

    // ── crearEvento ───────────────────────────────────────────────────────────

    @Test
    void crearEvento_notificarEstudiante_creaUnaNotificacion() {
        NotificacionEventoDTO evento = new NotificacionEventoDTO();
        evento.setEstudianteIdUsuario(10L);
        evento.setNotificarEstudiante(true);
        evento.setNotificarApoderados(false);
        evento.setTipoNotificacion("ASISTENCIA");
        evento.setAsunto("Asistencia registrada");
        evento.setMensaje("Faltó el día de hoy");

        when(historialRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        List<HistorialNotificacion> creadas = service.crearEvento(evento);

        assertThat(creadas).hasSize(1);
        assertThat(creadas.get(0).getUsuarioDestinatario()).isEqualTo(10L);
        assertThat(creadas.get(0).isLeida()).isFalse();
        verify(historialRepository, times(1)).save(any());
    }

    @Test
    void crearEvento_notificarApoderados_creaNotificacionPorApoderado() {
        NotificacionEventoDTO evento = new NotificacionEventoDTO();
        evento.setEstudianteIdUsuario(10L);
        evento.setNotificarEstudiante(false);
        evento.setNotificarApoderados(true);
        evento.setTipoNotificacion("CONDUCTA");
        evento.setAsunto("Anotación de conducta");
        evento.setMensaje("Interrumpió la clase");

        when(usuarioClient.obtenerApoderadosIds(10L)).thenReturn(List.of(20L, 21L));
        when(historialRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        List<HistorialNotificacion> creadas = service.crearEvento(evento);

        assertThat(creadas).hasSize(2);
        verify(historialRepository, times(2)).save(any());
    }

    @Test
    void crearEvento_sinDestinatarios_retornaListaVacia() {
        NotificacionEventoDTO evento = new NotificacionEventoDTO();
        evento.setEstudianteIdUsuario(10L);
        evento.setNotificarEstudiante(false);
        evento.setNotificarApoderados(false);

        List<HistorialNotificacion> creadas = service.crearEvento(evento);
        assertThat(creadas).isEmpty();
    }

    // ── helper ────────────────────────────────────────────────────────────────

    private HistorialNotificacion notificacion(Long id, Long destinatario, boolean leida) {
        HistorialNotificacion n = new HistorialNotificacion();
        n.setId(id);
        n.setUsuarioDestinatario(destinatario);
        n.setLeida(leida);
        n.setFechaEnvio(LocalDateTime.now());
        return n;
    }
}
