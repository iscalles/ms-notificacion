# ms-notificacion — Colegio Bernardo O'Higgins

Microservicio de notificaciones del sistema **Libro de Clases Digital** (DSY1106 Fullstack III).  
Gestiona el historial de notificaciones generadas por eventos del sistema (asistencia, conducta, calificaciones) y las entrega a estudiantes y apoderados.

---

## Responsabilidad

Este microservicio **no genera eventos por sí solo** — recibe eventos desde otros microservicios (ms-asistencia, ms-academico) a través del BFF y los transforma en notificaciones persistidas para cada destinatario.

```
ms-asistencia ──┐
                ├──► POST /notificaciones/evento ──► ms-notificacion ──► Oracle DB
ms-academico  ──┘                                        │
                                                         │ (consulta apoderados)
                                                         ▼
                                                    ms-usuario (8081)
```

---

## Endpoints

Base path: `/notificaciones` · Puerto: `8085`

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/notificaciones` | Lista todas las notificaciones |
| `GET` | `/notificaciones/{id}` | Busca una notificación por ID |
| `DELETE` | `/notificaciones/{id}` | Elimina una notificación |
| `POST` | `/notificaciones/evento` | Crea notificaciones a partir de un evento del sistema |
| `GET` | `/notificaciones/usuario/{idUsuario}` | Lista notificaciones de un usuario (orden desc por fecha) |
| `GET` | `/notificaciones/usuario/{idUsuario}/no-leidas/count` | Cuenta notificaciones no leídas del usuario |
| `PUT` | `/notificaciones/{id}/marcar-leida` | Marca una notificación como leída |
| `PUT` | `/notificaciones/usuario/{idUsuario}/marcar-todas-leidas` | Marca todas las notificaciones del usuario como leídas |

### POST `/notificaciones/evento` — cuerpo de la petición

```json
{
  "tipoNotificacion": "ASISTENCIA",
  "estudianteIdUsuario": 10,
  "idReferencia": 55,
  "asunto": "Inasistencia registrada",
  "mensaje": "El alumno no asistió el día de hoy.",
  "notificarEstudiante": true,
  "notificarApoderados": true
}
```

El servicio consulta a **ms-usuario** los IDs de los apoderados vinculados al estudiante y crea una notificación independiente por cada destinatario.

| Campo | Tipo | Descripción |
|---|---|---|
| `tipoNotificacion` | `String` | `ASISTENCIA`, `CONDUCTA`, `CALIFICACION`, etc. |
| `estudianteIdUsuario` | `Long` | ID del estudiante que originó el evento |
| `idReferencia` | `Long` | ID del registro que disparó la notificación (ej: idAsistencia) |
| `notificarEstudiante` | `boolean` | Si `true`, el estudiante recibe notificación |
| `notificarApoderados` | `boolean` | Si `true`, cada apoderado vinculado recibe notificación |

---

## Modelo de datos

### `HistorialNotificacion`

| Campo | Tipo | Descripción |
|---|---|---|
| `id_notificacion` | `Long` (PK) | Identificador único (secuencia Oracle) |
| `usuarioDestinatario` | `Long` | ID del usuario que recibe la notificación |
| `tipoNotificacion` | `String` (20) | Categoría del evento |
| `idReferencia` | `Long` | ID del registro relacionado (nullable) |
| `asunto` | `String` (200) | Título breve de la notificación |
| `mensaje` | `String` (LOB) | Cuerpo completo del mensaje |
| `leida` | `boolean` | `false` al crear, `true` al marcar leída |
| `fechaEnvio` | `LocalDateTime` | Momento de creación (zona: America/Santiago) |
| `fechaLectura` | `LocalDateTime` | Momento en que fue leída (nullable) |

---

## Configuración

```properties
# application.properties
spring.application.name=notificacionService
server.port=8085

# Base de datos Oracle Autonomous Database
spring.datasource.url=jdbc:oracle:thin:@proyectolibroasistencia_high?TNS_ADMIN=<ruta_wallet>
spring.datasource.username=ms_notificacion
spring.datasource.driver-class-name=oracle.jdbc.driver.OracleDriver

# Pool reducido (tier gratuito compartido entre microservicios)
spring.datasource.hikari.maximum-pool-size=3
spring.datasource.hikari.minimum-idle=1

# ms-usuario (para resolver apoderados del estudiante)
ms-usuario.url=http://localhost:8081

# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.OracleDialect
```

---

## Dependencias con otros microservicios

| Microservicio | Tipo | Para qué |
|---|---|---|
| **ms-usuario** (8081) | Feign Client (`UsuarioClient`) | Obtener los IDs de apoderados vinculados a un estudiante al crear un evento |
| **BFF** (8080) | Consumidor | Redirige los POST `/notificaciones/evento` desde ms-asistencia y ms-academico |

---

## Ejecución

```bash
# Desde la carpeta notificacionService/
./mvnw spring-boot:run
```

> Requiere conectividad con Oracle Autonomous Database y el Wallet configurado en la ruta indicada en `application.properties`.

---

## Tests unitarios

```bash
./mvnw test -Dtest="HistorialNotificacionServiceImplTest" -Dsurefire.failIfNoSpecifiedTests=false
```

| Clase | Tests | Casos cubiertos |
|---|---|---|
| `HistorialNotificacionServiceImplTest` | 12 | Listar, buscar por ID, eliminar (existente y no existente), marcar leída, marcar todas leídas, contar no leídas, crear evento (solo estudiante, solo apoderados, sin destinatarios) |

Los tests usan `@ExtendWith(MockitoExtension.class)` — no requieren base de datos ni Spring context.

---

## Stack tecnológico

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 21 | Lenguaje |
| Spring Boot | 3.2.12 | Framework base |
| Spring Data JPA | 3.x | Acceso a base de datos |
| Oracle JDBC | (via ojdbc11) | Driver para Oracle Autonomous DB |
| OpenFeign | (via spring-cloud) | Cliente HTTP para ms-usuario |
| JUnit 5 + Mockito | (via spring-boot-starter-test) | Tests unitarios |
