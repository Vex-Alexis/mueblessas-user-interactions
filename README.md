# 📊 Sistema de Registro de Estadísticas de Interacciones

Este proyecto representa una aplicación basada en un microservicio, diseñada para procesar estadísticas de interacciones de usuarios, almacenar los datos en una base NoSQL (DynamoDB) y publicar eventos en una cola de mensajería (RabbitMQ). La solución está construida siguiendo los principios de la Arquitectura Limpia, empleando el Scaffold de Bancolombia. El proyecto está completamente dockerizado, permitiendo su despliegue local de forma rápida y sencilla.

<br> <!-- Salto de línea -->

## 🚀 Funcionalidades

- Recepción de estadísticas de interacciones vía API REST
- Validación de integridad mediante hash MD5
- Persistencia de datos en Amazon DynamoDB (Local)
- Publicación de eventos en RabbitMQ si el guardado es exitoso
- Manejo de errores controlado con tipos de excepciones específicas
- Pruebas unitarias

<br> <!-- Salto de línea -->

## 🛠️ Tecnologías

- Java 21
- Spring WebFlux
- Reactor (programación reactiva)
- Amazon DynamoDB (Local)
- RabbitMQ (Local)
- Docker y Docker Compose
- Scaffold Bancolombia (Arquitectura Limpia)

<br> <!-- Salto de línea -->


## 📁 Estructura del Proyecto

```css
application/
  └─ app-service/               <- Arma y configura toda la aplicación (Inyecta dependencias y ejecuta el main).

domain/
  └─ model/                      <- Entidades del dominio y gateways (puertos)
  └─ useCase/                    <- Casos de uso que contienen la lógica y reglas de negocio.

infrastructure/
  └─ driven-adapters/            <- Adaptadores, implementan puertos para conexiones externas (DB, APIs, Producer, etc)
  └─ entry-points/               <- Puntos de entrada (como controladores REST, Kafka, GraphQL, consumer, etc) 
```

<br> <!-- Salto de línea -->


## 📢 Ejecutar proyecto

### Requisitos: 
- Java 21
- Docker Compose

### Clonar y levantar

1. Clona el repositorio:
```bash
git clone https://github.com/Vex-Alexis/mueblessas-user-interactions.git
```
<br> <!-- Salto de línea -->
2. Navega al directorio del proyecto:
```bash
cd mueblessas-user-interactions
```
3. Levanta los servicios:
```bash
docker-compose up --build
```

#### Esto levantará:
| Servicio                               | Puerto
|----------------------------------------|------
| Microservicio   `user-interactions`    | puerto `8080`
| Dynamodb-local                         | puerto `8081`
| RabbitMQ                               | puertos `5672` (AMQP) y `15672` (UI)

> Asegúrate que estos puertos no estén siendo usados por otros procesos.

> El Microservicio `user-interactions` estará disponible en: `http://localhost:8080`

<br> <!-- Salto de línea -->

## ⚙️ Configuraciones Iniciales
Una vez los servicios estén corriendo, realiza las siguientes configuraciones iniciales.

### 1. Crear cola en RabbitMQ
Para que el servicio pueda publicar eventos correctamente, asegúrate de que exista la cola `event.stats.validated`. Si no existe, créala manualmente desde la consola de administración de RabbitMQ:

##### Pasos:
1. Abre http://localhost:15672
2. Ingresa con:
    - usuario: guest
    - contraseña: guest
3. En el menú superior, ve a Queues and Streams → Add a new queue
4. Configura los siguientes valores:
    - Tipo: `classic`
    - Nombre: `event.stats.validated`
5. Haz clic en Add queue

> 💡 Puedes verificar que los eventos estén llegando correctamente haciendo clic en la cola creada y luego en Get Messages.

<br> <!-- Salto de línea -->

## 🌐 Flujo de la Aplicación
1. Se recibe una petición POST /api/stats con los datos de interacciones.
2. Se valida el hash MD5 del contenido.
3. Si el hash es válido:
   - Se almacena en DynamoDB
   - Se publica un evento a RabbitMQ
4. Si el hash es inválido o ocurre un error:
   - Se responde mensaje de error

<br> <!-- Salto de línea -->


## 📬 Endpoint Expuesto

| Método | Endpoint                              | Descripción                                      |
|--------|---------------------------------------|--------------------------------------------------|
| POST   | `/api/stats`                          | Recepción de estadísticas y procesamiento        |
| GET    | `/api/stats/test`                     | Verificación de disponibilidad del servicio      |


### Detalle del endpoint (`/api/stats`):

- Método: POST
- Ruta: `http://localhost:8080/api/stats`
- Body de ejemplo:

```json
{
  "totalContactoClientes": 300,
  "motivoReclamo": 15,
  "motivoGarantia": 20,
  "motivoDuda": 50,
  "motivoCompra": 80,
  "motivoFelicitaciones": 5,
  "motivoCambio": 10,
  "hash": "2692af3c8d7a895a40bb0be1fd160c3b"
}
```
- Respuesta esperada:
```json
{
    "httpStatus": 201,
    "success": true,
    "message": "Estadística almacenada en DynamoDB y evento enviado a RabbitMQ",
    "timestamp": "2025-07-07 14:58:42",
    "data": {
        "totalContactoClientes": 300,
        "motivoReclamo": 15,
        "motivoGarantia": 20,
        "motivoDuda": 50,
        "motivoCompra": 80,
        "motivoFelicitaciones": 5,
        "motivoCambio": 10,
        "hash": "2692af3c8d7a895a40bb0be1fd160c3b",
        "timestamp": "2025-07-07 14:58:42.556"
    }
}
```

<br> <!-- Salto de línea -->

## 🧯 Manejo de Errores
La API maneja los errores de forma controlada y descriptiva, devolviendo una estructura estándar en las respuestas fallidas:
```json
{
  "httpStatus": 400,
  "success": false,
  "message": "No fue posible procesar la estadística",
  "timestamp": "2025-07-08 02:45:01",
  "errorDetail": "Hash inválido para los datos proporcionados"
}
```

### Manejadores personalizados
El flujo de errores está definido usando operadores como .onErrorResume() en la capa Handler, y se pueden capturar los siguientes tipos de error:

| Tipo de Error          | Código HTTP       | Descripción                                      |
|------------------------|-------------------|--------------------------------------------------|
| InvalidHashException   | 400               | Recepción de estadísticas y procesamiento        |
| DataPersistenceException   | 500               | Error al guardar en DynamoDB        |
| MessagePublishingException   | 503               | Error al enviar evento a RabbitMQ        |
| Throwable (genérico)   | 500               | Cualquier otro error inesperado     |

<br> <!-- Salto de línea -->

##  🧪 Pruebas

El proyecto contiene una cobertura de pruebas >70%, distribuidas de la siguiente forma:

### ✅ Pruebas Unitarias

Aplicadas a:
- Casos de uso (usecase)
- Adaptadores (dynamo-db, mq-sender)
- Mapper y lógica del handler (reactive-web)

#### Ejecuta las pruebas con el siguiente comando:
```bash
./gradlew test
```
---

<br> <!-- Salto de línea -->

## 👤 Author
### Alexis Chavarría
Email: alexischavarria@hotmail.com
<br> <!-- Salto de línea -->

