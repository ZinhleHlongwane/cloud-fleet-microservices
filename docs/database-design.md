# Database design

Cloud Fleet uses PostgreSQL 16 with one service-owned schema per microservice.

Current schemas:

- `drone`
- `mission`
- `maintenance`
- `telemetry`
- `alert`

Each Spring Boot service connects to its own schema while sharing one PostgreSQL instance in the local Docker environment.

## Design principles

- Each service owns its database schema.
- Flyway manages versioned schema migrations.
- Hibernate validates the database schema rather than creating it automatically.
- Business constraints are enforced both in Java validation and SQL.
- Frequently queried access paths are supported by indexes.
- Operational timestamps use PostgreSQL `TIMESTAMPTZ`.
- Database changes are added through new Flyway migrations rather than editing migrations that have already been applied.

## Drone schema

Important constraints include:

- `drone_id` primary key
- unique `serial_number`
- battery level between 0 and 100
- payload greater than zero
- valid latitude and longitude ranges

Important indexes:

```sql
idx_drones_status
idx_drones_battery
idx_drones_status_battery_payload