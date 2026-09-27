# Troubleshooting playbook

## Service health

Each Spring Boot service exposes an Actuator health endpoint.

Check Drone Service:

```bash
curl http://localhost:8081/actuator/health