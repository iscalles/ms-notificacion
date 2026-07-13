package ms_notificacion.notificacionservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ms-usuario", url = "${ms-usuario.url}")
public interface UsuarioClient {

    @GetMapping("/apoderado-estudiante/estudiante-usuario/{idUsuarioEstudiante}/apoderados-ids")
    List<Long> obtenerApoderadosIds(@PathVariable("idUsuarioEstudiante") Long idUsuarioEstudiante);
}
