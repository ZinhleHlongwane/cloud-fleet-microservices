-- Run inside psql against the drone schema.
SET search_path TO drone;

EXPLAIN (ANALYZE, BUFFERS)
SELECT drone_id, model, battery_level, max_payload_kg
FROM drones
WHERE status = 'AVAILABLE'
  AND battery_level >= 30
  AND max_payload_kg >= 2.0
ORDER BY battery_level DESC
LIMIT 10;

-- The composite index created by Flyway is:
-- idx_drones_status_battery_payload(status, battery_level DESC, max_payload_kg)
--
-- Capture the real execution plan before/after any index experiment and record
-- measured timings in docs/query-optimisation.md. Do not invent benchmark numbers.
