# Query optimisation exercise

This repository includes `scripts/query-performance.sql` so performance work can be demonstrated with real PostgreSQL execution plans.

## Recommended workflow

1. Start PostgreSQL and populate several thousand drone rows.
2. Record an `EXPLAIN (ANALYZE, BUFFERS)` plan.
3. Temporarily drop the composite drone-selection index.
4. Repeat the plan and record the actual timing / scan type.
5. Recreate the index using the Flyway definition.
6. Repeat the plan.
7. Record the measured result below.

| Experiment | Planning time | Execution time | Scan type | Buffers |
|---|---:|---:|---|---|
| Without composite index | _measure locally_ | _measure locally_ | _record plan_ | _record_ |
| With composite index | _measure locally_ | _measure locally_ | _record plan_ | _record_ |

The table intentionally contains no invented performance numbers.
