# Cloud Fleet

Cloud Fleet is an enterprise-style drone fleet management platform built with Java and Spring Boot microservices.

The project demonstrates backend engineering, PostgreSQL database design, systems integration, asynchronous messaging, observability, containerisation, and database performance optimisation in one distributed system.

## What the platform does

Cloud Fleet manages the operational lifecycle of a drone fleet.

It can:

- register and manage drones
- find eligible drones based on battery level and payload capacity
- create and manage delivery missions
- assign available drones to missions
- progress missions through assignment, flight, and completion
- schedule and complete drone maintenance
- automatically detect overdue maintenance
- ingest and retain drone telemetry
- detect low-battery conditions
- publish operational events through ActiveMQ
- create and acknowledge fleet alerts
- expose health and Prometheus metrics
- visualise platform metrics in Grafana
- manage PostgreSQL schemas using Flyway migrations

## Architecture

Cloud Fleet consists of five Spring Boot services.

| Service | Responsibility | Port |
|---|---|---:|
| Drone Service | Drone inventory, status, payload and battery management | 8081 |
| Mission Service | Mission lifecycle and drone assignment | 8082 |
| Maintenance Service | Maintenance scheduling and overdue detection | 8083 |
| Telemetry Service | Drone telemetry ingestion and history | 8084 |
| Alert Service | Central operational alert management | 8085 |
| Prometheus | Metrics collection | 9090 |
| Grafana | Monitoring dashboards | 3000 |
| ActiveMQ Console | Message broker administration | 8161 |
| PostgreSQL | Persistent data storage | 5432 |

The services use both synchronous REST communication and asynchronous event-driven messaging.

Example:

```text
Mission Service
      |
      | REST
      v
Drone Service

Telemetry Service ----\
                       \
Maintenance Service ----> ActiveMQ ----> Alert Service
                       /
Mission Service -------/
```

See [docs/architecture.md](docs/architecture.md).

## Technology stack

### Backend

- Java 21
- Spring Boot 3.5
- Spring Web
- Spring Data JPA
- Jakarta Validation
- Maven

### Database

- PostgreSQL 16
- Flyway
- SQL constraints
- composite indexes
- partial indexes
- JPA repository queries
- HikariCP connection pooling

### Integration

- REST APIs
- ActiveMQ
- Spring JMS
- event-driven messaging

### Observability

- Spring Boot Actuator
- Micrometer
- Prometheus
- Grafana

### Testing

- JUnit 5
- Mockito

### Infrastructure

- Docker
- Docker Compose

## Running the platform

### Prerequisites

Install:

- Java 21
- Maven 3.9+
- Docker Desktop

### Build

From the project root:

```bash
mvn clean package
```

### Start

```bash
docker compose up --build
```

The services may take a short time to become ready after the containers start.

Check health with:

```bash
curl http://localhost:8081/actuator/health
```

Repeat for ports `8082` through `8085`.

## Example workflow

### 1. Register a drone

```http
POST http://localhost:8081/api/drones
Content-Type: application/json
```

```json
{
  "droneId": "DRN-001",
  "serialNumber": "CF-DRONE-001",
  "model": "AeroX-1",
  "status": "AVAILABLE",
  "batteryLevel": 92,
  "maxPayloadKg": 5.50,
  "latitude": -26.2041,
  "longitude": 28.0473
}
```

### 2. Create a mission

```http
POST http://localhost:8082/api/missions
Content-Type: application/json
```

```json
{
  "origin": "Sandton",
  "destination": "Midrand",
  "payloadKg": 2.5,
  "priority": "HIGH"
}
```

### 3. Assign a drone

```http
PUT http://localhost:8082/api/missions/1/assign
```

Mission Service queries Drone Service for an eligible drone.

The selected drone changes from:

```text
AVAILABLE -> ASSIGNED
```

### 4. Start the mission

```http
PUT http://localhost:8082/api/missions/1/start
```

The drone changes to:

```text
IN_FLIGHT
```

### 5. Complete the mission

```http
PUT http://localhost:8082/api/missions/1/complete
```

The mission becomes `COMPLETED`, and the drone becomes `AVAILABLE` again.

Mission lifecycle events are published through ActiveMQ and consumed by Alert Service.

## Telemetry and automatic alerts

Telemetry Service accepts operational drone readings.

Example:

```http
POST http://localhost:8084/api/telemetry
Content-Type: application/json
```

```json
{
  "droneId": "DRN-001",
  "batteryLevel": 18,
  "latitude": -26.2041,
  "longitude": 28.0473,
  "altitudeMeters": 120.0,
  "speedKph": 48.5
}
```

When the battery level is `20` or below, Telemetry Service publishes:

```text
BatteryLow
```

through ActiveMQ.

Alert Service consumes the event and persists a `WARNING` alert.

Example:

```http
GET http://localhost:8085/api/alerts
```

Alerts can also be acknowledged:

```http
PUT http://localhost:8085/api/alerts/4/acknowledge
```

And filtered:

```http
GET http://localhost:8085/api/alerts?acknowledged=true
```

## Maintenance automation

Maintenance Service supports scheduled maintenance records.

An overdue record is one where:

```text
scheduled date < today
AND
completed date is null
```

A scheduled background job checks for overdue maintenance and publishes a:

```text
MaintenanceDue
```

event through ActiveMQ.

Alert Service converts this event into a `WARNING`.

When maintenance is completed:

```http
PUT http://localhost:8083/api/maintenance/1/complete
```

Maintenance Service publishes:

```text
MaintenanceCompleted
```

The completed record is then removed from the overdue result set.

## Event-driven architecture

ActiveMQ is used as the asynchronous messaging layer.

Examples of events currently produced by the platform include:

- `MissionAssigned`
- `MissionStarted`
- `MissionCompleted`
- `MissionFailed`
- `BatteryLow`
- `InvalidTelemetry`
- `MaintenanceDue`
- `MaintenanceCompleted`

Alert Service consumes fleet events from:

```text
fleet.events.queue
```

Alert severity is derived from event type.

Examples:

```text
MissionFailed       -> CRITICAL
DroneOffline        -> CRITICAL
BatteryLow          -> WARNING
MaintenanceDue      -> WARNING
InvalidTelemetry    -> WARNING
MissionCompleted    -> INFO
```

## Database architecture

Cloud Fleet uses one PostgreSQL instance with separate service-owned schemas.

Examples:

```text
drone
mission
maintenance
telemetry
alert
```

Each service owns and migrates its own database schema using Flyway.

This keeps service persistence logically separated while allowing the complete platform to run locally using a single PostgreSQL container.

## Database engineering

The project treats PostgreSQL as an engineering component rather than simply application storage.

Implemented features include:

- Flyway versioned migrations
- primary and unique constraints
- `CHECK` constraints
- service-owned schemas
- composite indexes
- descending timestamp indexes
- partial PostgreSQL indexes
- query-plan analysis
- database connection monitoring
- HikariCP metrics

### Example optimisation

Maintenance Service frequently searches for unfinished overdue records.

A Flyway `V2` migration introduced partial indexes:

```sql
CREATE INDEX IF NOT EXISTS idx_maintenance_overdue_active
    ON maintenance_records(scheduled_date)
    WHERE completed_date IS NULL;

CREATE INDEX IF NOT EXISTS idx_maintenance_pending_alert
    ON maintenance_records(scheduled_date)
    WHERE completed_date IS NULL
      AND alert_sent = FALSE;
```

The indexes target only rows relevant to operational maintenance queries rather than indexing every completed record.

### Query-plan testing

The indexes were validated using PostgreSQL:

```sql
EXPLAIN ANALYZE
SELECT *
FROM maintenance.maintenance_records
WHERE completed_date IS NULL
  AND scheduled_date < CURRENT_DATE;
```

With a very small dataset PostgreSQL correctly selected a sequential scan because scanning the table was cheaper.

After generating a larger test dataset, PostgreSQL changed the plan to:

```text
Index Scan using idx_maintenance_overdue_active
```

This demonstrates that index usefulness depends on query selectivity, table size, and planner cost rather than assuming an index should always be used.

The synthetic performance-test data was removed after testing.

See:

- [Database design](docs/database-design.md)
- [Query optimisation](docs/query-optimisation.md)
- [Troubleshooting](docs/troubleshooting.md)

## Observability

Every Spring Boot service exposes:

```text
/actuator/health
/actuator/metrics
/actuator/prometheus
```

Prometheus scrapes all five services every 15 seconds.

Current monitored targets:

```text
drone-service:8081
mission-service:8082
maintenance-service:8083
telemetry-service:8084
alert-service:8085
```

Prometheus:

```text
http://localhost:9090
```

Grafana:

```text
http://localhost:3000
```

## Grafana dashboard

Grafana is provisioned automatically when Docker Compose starts.

The repository contains a **Cloud Fleet Overview** dashboard.

The dashboard currently displays:

- number of services currently up
- HTTP requests per second by service
- JVM heap memory usage
- active database connections

The `Services Up` panel reports all five Spring Boot services through Prometheus.

Grafana provisioning files are stored under:

```text
monitoring/grafana/
```

This means the dashboard and Prometheus datasource can be recreated automatically rather than requiring manual configuration.

## Metrics

Examples of available metrics include:

```text
http_server_requests_seconds
http_client_requests_seconds
jvm_memory_used_bytes
process_cpu_usage
hikaricp_connections_active
spring_data_repository_invocations_seconds
jms_message_publish_seconds
```

These allow the platform to monitor application performance, database connection usage, JVM health, REST communication, repository operations, and asynchronous messaging.

## Testing

Run all tests with:

```bash
mvn clean test
```

Individual modules can also be tested independently.

For example:

```bash
mvn -pl maintenance-service -am package
```

The project contains unit tests across the service modules.

## Containerised environment

Docker Compose starts:

```text
PostgreSQL
ActiveMQ
Drone Service
Mission Service
Maintenance Service
Telemetry Service
Alert Service
Prometheus
Grafana
```

This allows the complete distributed platform to run locally using one command.

## Design documentation

Additional technical documentation is available under `docs/`:

- [Architecture](docs/architecture.md)
- [Database design](docs/database-design.md)
- [Query optimisation](docs/query-optimisation.md)
- [Testing strategy](docs/testing-strategy.md)
- [Troubleshooting](docs/troubleshooting.md)
- [Cloud deployment](docs/cloud-deployment.md)
- [Design decisions](docs/design-decisions.md)

## Roadmap

Potential future extensions include:

- JWT authentication
- role-based authorisation
- API gateway
- Resilience4j retries and circuit breakers
- ActiveMQ dead-letter handling
- idempotent event consumers
- expanded integration testing
- React operations dashboard
- cloud deployment
- automated container image publishing

## Portfolio focus

Cloud Fleet was built to demonstrate practical experience with:

- Java backend development
- Spring Boot microservices
- REST API design
- PostgreSQL
- SQL
- database migrations
- query optimisation
- event-driven architecture
- systems integration
- Docker
- Prometheus
- Grafana
- application monitoring
- distributed backend systems

The project is relevant to junior and graduate roles in:

- Backend Engineering
- Java / Spring Boot Development
- Database Engineering
- Systems Integration
- Cloud / Platform Engineering