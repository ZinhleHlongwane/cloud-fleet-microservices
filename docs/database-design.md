# Database design

The local environment uses PostgreSQL 16 and one schema per service.

Key design choices:
- Service-owned schemas reduce accidental cross-service coupling.
- Flyway owns schema migrations. Hibernate runs with `ddl-auto=validate`.
- Business constraints exist both in Java validation and SQL checks.
- High-frequency access paths have indexes.
- Audit timestamps are stored in UTC-compatible `TIMESTAMPTZ`.

## Important indexes

### Drone selection
`idx_drones_status_battery_payload(status, battery_level DESC, max_payload_kg)`

Supports mission assignment queries that need available drones with enough battery and payload capacity.

### Telemetry
`idx_telemetry_drone_recorded(drone_id, recorded_at DESC)`

Supports "latest telemetry" and recent history retrieval.

### Maintenance
`idx_maintenance_due(completed_date, scheduled_date, alert_sent)`

Supports the overdue maintenance scan.

### Alerts
`idx_alerts_ack_created(acknowledged, created_at DESC)`

Supports operations dashboards showing unacknowledged alerts.
