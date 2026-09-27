CREATE TABLE IF NOT EXISTS telemetry_samples (
    id BIGSERIAL PRIMARY KEY,
    drone_id VARCHAR(40) NOT NULL,
    battery_level INTEGER NOT NULL CHECK (battery_level BETWEEN 0 AND 100),
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    altitude_meters DOUBLE PRECISION CHECK (altitude_meters IS NULL OR altitude_meters >= 0),
    speed_kph DOUBLE PRECISION CHECK (speed_kph IS NULL OR speed_kph >= 0),
    recorded_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_telemetry_drone_recorded ON telemetry_samples(drone_id, recorded_at DESC);
CREATE INDEX IF NOT EXISTS idx_telemetry_recorded ON telemetry_samples(recorded_at DESC);
