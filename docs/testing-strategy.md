# Testing strategy

Cloud Fleet uses a mix of automated unit tests, service-level verification, and end-to-end integration checks.

## Automated tests

Current automated tests cover:

- Drone Service business logic
- Mission lifecycle behaviour
- Maintenance record behaviour
- Telemetry low-battery event publishing
- Alert acknowledgement behaviour

The Maven build runs these tests as part of:

```bash
mvn clean test