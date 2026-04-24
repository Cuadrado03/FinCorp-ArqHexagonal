# FinCorp — Arquitectura Hexagonal (Entrega 3 Refactorizada)

Proyecto de microservicios bajo **Arquitectura Hexagonal** para el sistema de créditos corporativos FinCorp.

---

## 📐 Estructura del Proyecto

```
FinCorp_Refactored/
│
├── src/                                  ← Microservicio Principal (Spring Boot / Java 17)
│   └── main/java/com/fincorp/
│       ├── domain/                       ← CAPA DE DOMINIO (núcleo, sin dependencias externas)
│       │   ├── model/
│       │   │   ├── Credit.java
│       │   │   ├── CreditHistoryReport.java
│       │   │   └── SimulationResult.java
│       │   └── service/
│       │       ├── CreditApprovalService.java    ← Regla: monto ≤ 30% del salario
│       │       └── CreditHistoryAnalyzer.java    ← Análisis y credit score
│       │
│       ├── application/                  ← CAPA DE APLICACIÓN (casos de uso, puertos)
│       │   ├── port/
│       │   │   ├── in/
│       │   │   │   └── CreditUseCase.java        ← Puerto de entrada (driving port)
│       │   │   └── out/
│       │   │       ├── CreditRepository.java     ← Puerto de salida (driven port)
│       │   │       ├── CreditSimulationPort.java
│       │   │       └── NotificationPort.java
│       │   └── service/
│       │       └── CreditService.java            ← Orquestador de casos de uso
│       │
│       └── infrastructure/               ← CAPA DE INFRAESTRUCTURA (adaptadores)
│           ├── controller/
│           │   ├── CreditController.java         ← Adaptador de entrada (REST API)
│           │   └── dto/
│           │       └── CreateCreditRequest.java
│           ├── client/
│           │   ├── CreditSimulationHttpAdapter.java
│           │   └── dto/
│           ├── config/
│           │   └── BeanConfiguration.java
│           ├── notification/
│           │   └── EmailNotificationAdapter.java
│           └── persistence/
│               ├── adapter/CreditPersistenceAdapter.java
│               ├── entity/CreditEntity.java
│               └── repository/SpringDataCreditRepository.java
│
├── credit-simulator-service/             ← Microservicio Simulador (Python / FastAPI)
│   └── app/
│       ├── domain/model/
│       │   └── simulation.py             ← Calcula status, total_interest, total_to_pay
│       ├── application/
│       │   ├── port/out/simulation_repository.py
│       │   └── service/simulator_service.py
│       └── infrastructure/
│           ├── api/schemas.py
│           ├── persistence/sqlite_simulation_repository.py
│           └── main.py                   ← FastAPI + UI en español
│
├── docker-compose.yml                    ← Orquestación de contenedores
├── Dockerfile                            ← Build del microservicio Java
└── pom.xml
```

---

## 🔄 Cambios Realizados en esta Entrega

### 1. Refactorización de la API (Capa Infraestructura/Web)

**ELIMINADO:** `GET /credits/by-cedula/{employeeId}`

El endpoint redundante de búsqueda por cédula ha sido **eliminado** junto con el método `getByEmployeeId` del puerto `CreditUseCase` y del servicio de aplicación `CreditService`. La búsqueda por empleado queda consolidada en:

**CONSERVADO y PRIORIZADO:** `GET /credits/history/{employeeId}`

Este endpoint cumple ahora función de **búsqueda integral** retornando la lista de créditos del empleado más:
- Resumen estadístico (total, aprobados, rechazados)
- Credit score calculado por el dominio

#### Endpoints disponibles tras la refactorización:

| Método   | Endpoint                          | Descripción                                      |
|----------|-----------------------------------|--------------------------------------------------|
| `POST`   | `/credits`                        | Crear nuevo crédito                              |
| `GET`    | `/credits/all`                    | Listar todos los créditos                        |
| `GET`    | `/credits/{creditId}`             | Obtener crédito por ID                           |
| `GET`    | `/credits/history/{employeeId}`   | ✅ Historial inteligente por empleado (consolida búsqueda) |
| `PUT`    | `/credits/{id}`                   | Actualizar crédito                               |
| `DELETE` | `/credits/{id}`                   | Eliminar crédito                                 |

### 2. Evolución del Microservicio Simulador

- **UI en español** con cabecera: *"Simula tu crédito y piensa dos veces antes de tomar la decisión de ser parte de nuestra compañía FinCorp"*
- **Diseño mejorado**: interfaz moderna con colores corporativos, tarjetas y tipografía legible
- **Resultados claros:**
  - **Total a Pagar** = Capital + Intereses (destacado visualmente)
  - **Total de Intereses** desglosado
  - **Estado del Crédito:** `ACEPTADO` en **verde** / `RECHAZADO` en **rojo**
- **Arquitectura correcta:** el cálculo del estado (`ACEPTADO`/`RECHAZADO`) reside en el **Dominio** (`simulation.py → __post_init__`), no en el controlador

### 3. Estabilización Docker y Base de Datos

- `docker-compose.yml` con credenciales explícitas: `POSTGRES_USER: fincorp`, `POSTGRES_PASSWORD: fincorp`, `POSTGRES_DB: fincorp`
- Conectividad por nombre de servicio Docker: `jdbc:postgresql://postgres:5432/fincorp`
- Volumen persistente `postgres_data` para datos entre reinicios
- Healthcheck en Postgres con reintentos antes de levantar la app Java

---

## 🚀 Instrucciones de Docker

### Requisitos previos

- [Docker](https://docs.docker.com/get-docker/) ≥ 24
- [Docker Compose](https://docs.docker.com/compose/) ≥ 2.20

### Levantar todos los servicios

```bash
# Desde la raíz del proyecto (donde está docker-compose.yml)
docker compose up --build
```

La primera ejecución descargará imágenes y compilará. En ejecuciones posteriores:

```bash
docker compose up
```

### Verificar que todo está corriendo

```bash
docker compose ps
```

Resultado esperado:

```
NAME                 STATUS
fincorp-postgres     Up (healthy)
fincorp-simulator    Up
fincorp-mailhog      Up
fincorp-app          Up
```

### Acceder a los servicios

| Servicio                    | URL                                          |
|-----------------------------|----------------------------------------------|
| API REST principal          | http://localhost:8080                        |
| Swagger / OpenAPI           | http://localhost:8080/swagger-ui/index.html  |
| Simulador de Crédito (UI)   | http://localhost:8081/ui                     |
| Simulador (API)             | http://localhost:8081/simulations            |
| MailHog (bandeja de correo) | http://localhost:8025                        |

### Detener y limpiar

```bash
# Detener sin borrar datos
docker compose down

# Detener y borrar volúmenes (⚠️ borra la base de datos)
docker compose down -v
```

### Reconstruir un servicio específico

```bash
docker compose up --build fincorp-app
docker compose up --build simulator
```

### Ver logs

```bash
docker compose logs -f fincorp-app
docker compose logs -f simulator
docker compose logs -f postgres
```

---

## 🧪 Ejemplo de uso de la API

### Crear un crédito

```bash
curl -X POST http://localhost:8080/credits \
  -H "Content-Type: application/json" \
  -d '{
    "employeeId": 1001,
    "employeeName": "Ana García",
    "email": "ana@fincorp.co",
    "salary": 5000000,
    "amount": 1200000,
    "requestDate": "2026-04-23",
    "termMonths": 24
  }'
```

### Consultar historial del empleado

```bash
curl http://localhost:8080/credits/history/1001
```

Respuesta incluye: lista de créditos + resumen estadístico + credit score.

### Simular un crédito (API)

```bash
curl -X POST http://localhost:8081/simulations \
  -H "Content-Type: application/json" \
  -d '{
    "salary": 5000000,
    "requested_amount": 1200000,
    "term_months": 24
  }'
```

Respuesta incluye: `status`, `total_to_pay`, `total_interest`, `estimated_installment`.

---

## ⚙️ Variables de Entorno

### fincorp-app (Spring Boot)

| Variable                  | Valor en Docker                          | Valor por defecto (local)              |
|---------------------------|------------------------------------------|----------------------------------------|
| `SPRING_DATASOURCE_URL`   | `jdbc:postgresql://postgres:5432/fincorp`| `jdbc:postgresql://localhost:5432/fincorp` |
| `SPRING_DATASOURCE_USERNAME` | `fincorp`                             | `fincorp`                              |
| `SPRING_DATASOURCE_PASSWORD` | `fincorp`                             | `fincorp`                              |
| `SIMULATOR_BASE_URL`      | `http://simulator:8081`                  | `http://localhost:8081`                |
| `SPRING_MAIL_HOST`        | `mailhog`                                | `localhost`                            |

### simulator (FastAPI)

| Variable             | Valor por defecto              |
|----------------------|-------------------------------|
| `SIMULATOR_DB_PATH`  | `/data/simulations.db`        |

---

## 🏗️ Arquitectura Hexagonal — Principios aplicados

```
┌─────────────────────────────────────────────────────────┐
│                    INFRAESTRUCTURA                       │
│  ┌──────────────┐          ┌──────────────────────────┐ │
│  │ CreditController│       │ CreditPersistenceAdapter │ │
│  │ (REST Adapter)  │       │ EmailNotificationAdapter │ │
│  └──────┬───────┘          └───────────────┬──────────┘ │
│         │                                  │            │
│  ┌──────▼──────────────────────────────────▼──────────┐ │
│  │               APLICACIÓN (Casos de Uso)            │ │
│  │  CreditUseCase ← CreditService → CreditRepository  │ │
│  └──────────────────────┬──────────────────────────── ┘ │
│                         │                               │
│  ┌──────────────────────▼──────────────────────────── ┐ │
│  │                    DOMINIO                          │ │
│  │  Credit · CreditApprovalService · CreditHistory    │ │
│  │  Simulation (calcula status en __post_init__)      │ │
│  └────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────┘
```

**Regla de dependencia:** Las capas externas dependen de las internas. El Dominio no conoce ni Infraestructura ni Aplicación.