# Query optimisation exercise

Cloud Fleet includes a practical PostgreSQL query-optimisation exercise based on the Maintenance Service.

The goal was to verify whether partial indexes improve the overdue-maintenance queries used by the application.

## Query being optimised

Maintenance Service regularly searches for unfinished overdue records.

Example:

```sql
SELECT *
FROM maintenance.maintenance_records
WHERE completed_date IS NULL
  AND scheduled_date < CURRENT_DATE;