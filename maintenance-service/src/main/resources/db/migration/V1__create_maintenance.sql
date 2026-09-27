CREATE TABLE IF NOT EXISTS maintenance_records (
    id BIGSERIAL PRIMARY KEY,
    drone_id VARCHAR(40) NOT NULL,
    maintenance_type VARCHAR(80) NOT NULL,
    scheduled_date DATE NOT NULL,
    completed_date DATE,
    notes VARCHAR(1000),
    alert_sent BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_maintenance_drone_date ON maintenance_records(drone_id, scheduled_date DESC);
CREATE INDEX IF NOT EXISTS idx_maintenance_due ON maintenance_records(completed_date, scheduled_date, alert_sent);
