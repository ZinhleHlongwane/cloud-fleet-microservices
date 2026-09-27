# Architecture

Cloud Fleet is a distributed drone fleet management platform built as five independently deployable Spring Boot services.

Each service owns a separate PostgreSQL schema and exposes its own REST API and operational metrics.

## Services

| Service | Port | Responsibility | Database schema |
|---|---:|---|---|
| Drone Service | 8081 | Drone registry, availability, battery status and eligibility | `drone` |
| Mission Service | 8082 | Mission creation, assignment, lifecycle and drone coordination | `mission` |
| Maintenance Service | 8083 | Maintenance scheduling, completion and overdue detection | `maintenance` |
| Telemetry Service | 8084 | Telemetry ingestion, latest state and telemetry history | `telemetry` |
| Alert Service | 8085 | Fleet event consumption and operational alert management | `alert` |

Infrastructure services:

| Component | Port | Responsibility |
|---|---:|---|
| PostgreSQL | 5432 | Persistent storage |
| ActiveMQ | 61616 | Asynchronous messaging |
| ActiveMQ Console | 8161 | Broker administration |
| Prometheus | 9090 | Metrics collection |
| Grafana | 3000 | Metrics visualisation |

## Local database architecture

The local environment uses one PostgreSQL instance with separate schemas:

```text
drone
mission
maintenance
telemetry
alert