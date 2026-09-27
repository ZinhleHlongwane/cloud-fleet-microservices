# Design decisions

## PostgreSQL instead of H2
The runtime database is PostgreSQL so development behaviour matches the database used for performance work and cloud deployment.

## Flyway instead of automatic schema creation
Database change history is explicit and repeatable. Hibernate validates rather than mutates production schemas.

## Soft decommissioning
`DELETE /api/drones/{id}` changes a drone to `DECOMMISSIONED` instead of deleting operational history.

## REST + messaging
REST is used where an immediate answer is needed (mission assignment). ActiveMQ is used for operational events that can be handled asynchronously.

## One PostgreSQL instance locally
Each service owns a schema. This keeps Docker lightweight while preserving logical ownership. Production can split schemas into separate RDS/Cloud SQL databases without changing service contracts.
