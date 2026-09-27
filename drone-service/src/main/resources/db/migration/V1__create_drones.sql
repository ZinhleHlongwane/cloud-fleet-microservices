CREATE TABLE IF NOT EXISTS drones (
    drone_id VARCHAR(40) PRIMARY KEY,
    serial_number VARCHAR(80) NOT NULL UNIQUE,
    model VARCHAR(120) NOT NULL,
    status VARCHAR(30) NOT NULL,
    battery_level INTEGER NOT NULL CHECK (battery_level BETWEEN 0 AND 100),
    max_payload_kg NUMERIC(10,2) NOT NULL CHECK (max_payload_kg > 0),
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_latitude CHECK (latitude IS NULL OR latitude BETWEEN -90 AND 90),
    CONSTRAINT chk_longitude CHECK (longitude IS NULL OR longitude BETWEEN -180 AND 180)
);

CREATE INDEX IF NOT EXISTS idx_drones_status ON drones(status);
CREATE INDEX IF NOT EXISTS idx_drones_battery ON drones(battery_level);
CREATE INDEX IF NOT EXISTS idx_drones_status_battery_payload
    ON drones(status, battery_level DESC, max_payload_kg);
