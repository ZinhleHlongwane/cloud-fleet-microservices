# Implemented requirements

This build upgrades the original single-service starter into a distributed Cloud Fleet backend.

## Implemented now
- Multi-module Maven project
- Drone, Mission, Maintenance, Telemetry and Alert services
- PostgreSQL runtime database
- Separate service-owned PostgreSQL schemas
- Flyway migrations
- SQL constraints and indexes
- Drone CRUD-style lifecycle, pagination, filtering and eligibility query
- Mission lifecycle and drone assignment over REST
- Maintenance scheduling, completion and overdue scans
- Telemetry history and latest telemetry
- ActiveMQ event publishing / consuming
- Alert persistence and acknowledgement
- Structured REST error responses
- Jakarta Validation
- Spring Boot Actuator health and metrics
- Prometheus scraping configuration
- Grafana container
- Swagger/OpenAPI on every service
- Docker Compose for the platform
- Unit tests plus PostgreSQL Testcontainers integration test
- GitHub Actions CI
- Query optimisation exercise with EXPLAIN ANALYZE
- Database troubleshooting playbook
- AWS RDS Terraform example
- Cloud deployment design documentation

## Intentionally not claimed as complete yet
These were part of the longer-term/optional roadmap and should be implemented in later iterations rather than presented as finished:
- JWT authentication and role-based authorization
- API gateway
- Resilience4j retry / circuit breaker layer
- Dead-letter queue and idempotent consumer implementation
- React operations dashboard
- A real deployed AWS RDS or Google Cloud SQL environment
- Provisioned Grafana dashboards
- 50+ automated tests

The README labels these honestly as roadmap items.
