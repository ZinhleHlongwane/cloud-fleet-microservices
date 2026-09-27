CREATE TABLE IF NOT EXISTS missions (
    id BIGSERIAL PRIMARY KEY,
    drone_id VARCHAR(40),
    origin VARCHAR(160) NOT NULL,
    destination VARCHAR(160) NOT NULL,
    payload_kg NUMERIC(10,2) NOT NULL CHECK (payload_kg > 0),
    priority VARCHAR(20) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    started_at TIMESTAMPTZ,
    completed_at TIMESTAMPTZ
);

CREATE INDEX IF NOT EXISTS idx_missions_status_created ON missions(status, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_missions_drone_created ON missions(drone_id, created_at DESC);
