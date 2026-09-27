CREATE INDEX IF NOT EXISTS idx_maintenance_overdue_active
    ON maintenance_records(scheduled_date)
    WHERE completed_date IS NULL;

CREATE INDEX IF NOT EXISTS idx_maintenance_pending_alert
    ON maintenance_records(scheduled_date)
    WHERE completed_date IS NULL
    AND alert_sent = FALSE;