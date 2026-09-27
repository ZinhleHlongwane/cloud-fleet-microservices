# Testing strategy

Current automated tests cover:
- Drone creation, duplicate handling, updates and decommissioning.
- Mission lifecycle state transitions.
- Maintenance completion / alert state.
- Telemetry low-battery event publishing.
- Alert acknowledgement.
- PostgreSQL repository integration through Testcontainers when Docker is available.

Next growth target:
- Controller/MockMvc tests for every endpoint.
- ActiveMQ integration tests.
- Mission-to-drone service contract tests.
- Failure and timeout scenarios.
- Migration smoke tests across all service schemas.
