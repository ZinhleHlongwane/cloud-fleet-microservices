# Cloud Fleet

Cloud Fleet is an enterprise-style drone fleet management platform built as a set of Spring Boot services. It is designed to demonstrate backend engineering, database engineering, systems integration, observability, and cloud-readiness in one portfolio project.

## What the platform does

Cloud Fleet manages the operational lifecycle of a drone fleet:

- register and manage drones
- find eligible drones based on battery and payload capacity
- create and progress missions through a controlled lifecycle
- schedule and complete maintenance
- ingest telemetry and retain history
- publish operational events through ActiveMQ
- create and acknowledge fleet alerts
- expose health and Prometheus metrics for every service
- manage PostgreSQL schemas through Flyway migrations

## Architecture

| Service | Port |
|---|---:|
| Drone Service | 8081 |
| Mission Service | 8082 |
| Maintenance Service | 8083 |
| Telemetry Service | 8084 |
| Alert Service | 8085 |
| Prometheus | 9090 |
| Grafana | 3000 |
| ActiveMQ console | 8161 |
| PostgreSQL | 5432 |

See [docs/architecture.md](docs/architecture.md).

## Technology stack

- Java 21
- Spring Boot 3.5
- Spring Web
- Spring Data JPA
- Jakarta Validation
- PostgreSQL 16
- Flyway
- ActiveMQ
- Spring Boot Actuator
- Micrometer / Prometheus
- Grafana
- OpenAPI / Swagger UI
- JUnit 5 / Mockito
- Testcontainers
- Docker / Docker Compose
- GitHub Actions
- Terraform example for AWS RDS

## Run locally

### Prerequisites

- Java 21
- Maven 3.9+
- Docker Desktop

### Build

```bash
mvn clean package
```

### Start the platform

```bash
docker compose up --build
```

Swagger:
- http://localhost:8081/swagger-ui.html
- http://localhost:8082/swagger-ui.html
- http://localhost:8083/swagger-ui.html
- http://localhost:8084/swagger-ui.html
- http://localhost:8085/swagger-ui.html

Health:
- http://localhost:8081/actuator/health
- repeat for ports 8082-8085

## Example flow

### 1. Register a drone

```http
POST http://localhost:8081/api/drones
Content-Type: application/json

{
  "droneId": "DRN-001",
  "serialNumber": "CF-2026-001",
  "model": "Falcon X1",
  "status": "AVAILABLE",
  "batteryLevel": 92,
  "maxPayloadKg": 5.5,
  "latitude": -26.1076,
  "longitude": 28.0567
}
```

### 2. Create a mission

```http
POST http://localhost:8082/api/missions

{
  "origin": "Sandton",
  "destination": "Midrand",
  "payloadKg": 2.2,
  "priority": "HIGH"
}
```

### 3. Assign the mission

```http
PUT http://localhost:8082/api/missions/1/assign
```

Mission Service queries Drone Service for an eligible drone and changes the selected drone to `ASSIGNED`.

### 4. Send telemetry

```http
POST http://localhost:8084/api/telemetry

{
  "droneId": "DRN-001",
  "batteryLevel": 18,
  "latitude": -26.1,
  "longitude": 28.0,
  "altitudeMeters": 80,
  "speedKph": 42
}
```

A battery level of 20 or below publishes a `BatteryLow` event to ActiveMQ. Alert Service consumes it and creates an alert.

### 5. View alerts

```http
GET http://localhost:8085/api/alerts?acknowledged=false
```

## Database engineering

Cloud Fleet deliberately treats the database as an engineering concern, not just application storage.

Implemented:
- PostgreSQL runtime database
- separate service-owned schemas
- Flyway migrations
- SQL constraints
- composite and time-series indexes
- custom JPA queries
- connection-pool health endpoint
- query optimisation exercise using `EXPLAIN (ANALYZE, BUFFERS)`
- AWS RDS Terraform example
- troubleshooting queries for `pg_stat_database` and `pg_stat_activity`

See:
- [Database design](docs/database-design.md)
- [Query optimisation](docs/query-optimisation.md)
- [Troubleshooting](docs/troubleshooting.md)
- [Cloud deployment](docs/cloud-deployment.md)

## Testing

Run:

```bash
mvn clean test
```

The repository includes unit tests and a PostgreSQL Testcontainers integration test. The integration test automatically skips when Docker is unavailable.

See [docs/testing-strategy.md](docs/testing-strategy.md).

## Observability

Every service exposes:
- `/actuator/health`
- `/actuator/metrics`
- `/actuator/prometheus`

Prometheus is included in Docker Compose and Grafana is available at `http://localhost:3000`.

## API error format

Example:

```json
{
  "timestamp": "2026-09-27T10:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Drone not found: DRN-404",
  "path": "/api/drones/DRN-404",
  "validationErrors": {}
}
```

## Design documentation

- [Architecture](docs/architecture.md)
- [Database design](docs/database-design.md)
- [Query optimisation](docs/query-optimisation.md)
- [Testing strategy](docs/testing-strategy.md)
- [Troubleshooting](docs/troubleshooting.md)
- [Cloud deployment](docs/cloud-deployment.md)
- [Design decisions](docs/design-decisions.md)

## Roadmap

The current release establishes the distributed backend and database platform. Planned extensions:

- JWT authentication and role-based authorization
- API gateway
- Resilience4j retries, timeouts and circuit breakers
- ActiveMQ dead-letter handling and idempotent event consumers
- richer contract/integration test coverage
- React operations dashboard
- real AWS RDS or Google Cloud SQL deployment
- Grafana dashboards committed as provisioning files
- automated Docker image publishing

## Portfolio focus

This project is intentionally designed to demonstrate skills relevant to:

- Junior Backend Engineer
- Java / Spring Boot Developer
- Database Engineer / Database Engineering Intern
- Systems Integration Engineer
- Cloud / Platform Engineering graduate roles
