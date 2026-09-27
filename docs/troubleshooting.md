# Troubleshooting playbook

## Database health
- `GET http://localhost:8081/ops/database`
- `GET http://localhost:8081/actuator/health`
- Repeat `/actuator/health` for ports 8082-8085.

## PostgreSQL
```bash
docker compose logs postgres
docker compose exec postgres psql -U cloudfleet -d cloudfleet
```

Useful checks:
```sql
SELECT datname, numbackends, xact_commit, xact_rollback
FROM pg_stat_database
WHERE datname = 'cloudfleet';

SELECT pid, state, wait_event_type, query
FROM pg_stat_activity
WHERE datname = 'cloudfleet';
```

## ActiveMQ
Web console: http://localhost:8161 (admin/admin)

## Common failures
- Connection refused: verify container health and `DATABASE_URL`.
- Flyway validation failure: do not manually edit applied migration files; create a new migration.
- Mission cannot assign: check eligible drones at `/api/drones/eligible`.
- Alerts missing: verify ActiveMQ and Alert Service health.
