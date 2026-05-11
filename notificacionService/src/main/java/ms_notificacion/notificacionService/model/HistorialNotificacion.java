package ms_notificacion.notificacionservice.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
public class HistorialNotificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historial_notificacion_seq")
    @SequenceGenerator(name = "historial_notificacion_seq", sequenceName = "seq_historial_notificacion", allocationSize = 1)
    private Long id_notificacion;

    @Column(name = "USUARIO_DESTINATARIO", nullable = false)
    private Long usuarioDestinatario;

    @Column(name = "TIPO_CANAL", length = 20)
    private String tipoCanal;

    @Column(name = "ASUNTO", length = 200)
    private String asunto;

    @Lob
    @Column(name = "MENSAJE")
    private String mensaje;

    @JsonFormat(pattern="dd-MM-yyyy HH:mm",timezone = "America/Santiago")
    @Column(name = "FECHA_ENVIO")
    private LocalDateTime fechaEnvio;

    @Column(name = "ESTADO_ENVIO", length = 20)
    private String estadoEnvio;

    public HistorialNotificacion() {

    }


    public HistorialNotificacion(Long id_notificacion, Long usuarioDestinatario, String tipoCanal, String asunto, String mensaje, LocalDateTime fechaEnvio, String estadoEnvio) {
        this.id_notificacion = id_notificacion;
        this.usuarioDestinatario = usuarioDestinatario;
        this.tipoCanal = tipoCanal;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.fechaEnvio = fechaEnvio;
        this.estadoEnvio = estadoEnvio;
    }


    public String getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(String estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getTipoCanal() {
        return tipoCanal;
    }

    public void setTipoCanal(String tipoCanal) {
        this.tipoCanal = tipoCanal;
    }

    public Long getUsuarioDestinatario() {
        return usuarioDestinatario;
    }

    public void setUsuarioDestinatario(Long usuarioDestinatario) {
        this.usuarioDestinatario = usuarioDestinatario;
    }

    public Long getId() {
        return id_notificacion;
    }

    public void setId(Long id) {
        this.id_notificacion = id;
    }
}
