# Architecture

Cloud Fleet is split into independently deployable Spring Boot services.

| Service | Port | Responsibility | Database schema |
|---|---:|---|---|
| Drone Service | 8081 | Drone registry, availability, battery and eligibility | `drone` |
| Mission Service | 8082 | Mission lifecycle and drone assignment | `mission` |
| Maintenance Service | 8083 | Scheduling, completion and overdue maintenance | `maintenance` |
| Telemetry Service | 8084 | Time-series-like telemetry history and latest state | `telemetry` |
| Alert Service | 8085 | Consumes operational events and manages alerts | `alert` |

All services use one PostgreSQL server locally but separate schemas to preserve ownership boundaries.
In production the same design can move to separate databases/instances where required.

## Communication

- REST: Mission Service -> Drone Service for drone selection and status changes.
- ActiveMQ queue `fleet.events.queue`: mission, maintenance and telemetry events are consumed by Alert Service.
- Actuator/Prometheus: every service exposes health and metrics.

## Failure model

Synchronous REST calls have explicit failures rather than silently proceeding.
Async event producers are decoupled from alert persistence; alerts can recover after short service outages when broker delivery resumes.

Future resilience improvements are documented in the roadmap: retries, circuit breakers, dead-letter queues and idempotency keys.
