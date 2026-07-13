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

    @Column(name = "TIPO_NOTIFICACION", length = 20, nullable = false)
    private String tipoNotificacion;

    @Column(name = "ID_REFERENCIA")
    private Long idReferencia;

    @Column(name = "ASUNTO", length = 200)
    private String asunto;

    @Lob
    @Column(name = "MENSAJE")
    private String mensaje;

    @Column(name = "LEIDA", nullable = false)
    private boolean leida = false;

    @JsonFormat(pattern="dd-MM-yyyy HH:mm",timezone = "America/Santiago")
    @Column(name = "FECHA_ENVIO", nullable = false)
    private LocalDateTime fechaEnvio;

    @JsonFormat(pattern="dd-MM-yyyy HH:mm",timezone = "America/Santiago")
    @Column(name = "FECHA_LECTURA")
    private LocalDateTime fechaLectura;

    public HistorialNotificacion() {

    }

    public HistorialNotificacion(Long id_notificacion, Long usuarioDestinatario, String tipoNotificacion,
                                  Long idReferencia, String asunto, String mensaje, boolean leida,
                                  LocalDateTime fechaEnvio, LocalDateTime fechaLectura) {
        this.id_notificacion = id_notificacion;
        this.usuarioDestinatario = usuarioDestinatario;
        this.tipoNotificacion = tipoNotificacion;
        this.idReferencia = idReferencia;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.leida = leida;
        this.fechaEnvio = fechaEnvio;
        this.fechaLectura = fechaLectura;
    }

    public Long getId() {
        return id_notificacion;
    }

    public void setId(Long id) {
        this.id_notificacion = id;
    }

    public Long getUsuarioDestinatario() {
        return usuarioDestinatario;
    }

    public void setUsuarioDestinatario(Long usuarioDestinatario) {
        this.usuarioDestinatario = usuarioDestinatario;
    }

    public String getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(String tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    public Long getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(Long idReferencia) {
        this.idReferencia = idReferencia;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isLeida() {
        return leida;
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public LocalDateTime getFechaLectura() {
        return fechaLectura;
    }

    public void setFechaLectura(LocalDateTime fechaLectura) {
        this.fechaLectura = fechaLectura;
    }
}
