# 🚁 Cloud Fleet Microservices

**A REST API for tracking and managing a fleet of drones.**

Cloud Fleet Microservices is a Spring Boot application that exposes a simple, validated API for registering drones and keeping track of their status, battery level, and position.

---

## ✨ What it does

- **A real REST API**, built with Spring Boot, backed by an H2 database via Spring Data JPA.
- **Validated input**, enforced with Bean Validation — battery must be between 0 and 100, and required fields can't be blank.
- **A clean DTO/entity separation** — the API's request/response shape is decoupled from the database schema via a dedicated DTO and mapper layer.
- **Structured error responses** — a global exception handler turns validation failures into clear, field-by-field error messages instead of raw stack traces.
- **API documentation** generated automatically via springdoc-openapi.

---

## 🎮 Quick start

```bash
# Build and run
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

---

## 📡 Endpoints

| Method | Path      | What it does                          |
|--------|-----------|----------------------------------------|
| GET    | `/drones` | Returns a list of all registered drones |
| POST   | `/drones` | Registers a new drone                   |

### Example: registering a drone

```bash
curl -X POST http://localhost:8080/drones \
  -H "Content-Type: application/json" \
  -d '{
    "droneId": "DRONE-01",
    "status": "IDLE",
    "battery": 100,
    "currentX": 0,
    "currentY": 0
  }'
```

If `battery` is outside 0–100, or `droneId`/`status` is blank, the API responds with `400 Bad Request` and a message describing which field failed and why.

---

## 🏗️ Architecture

```
src/main/java/com/example/cloud_fleet_microservices/
├── Drone.java                 The JPA entity — what's actually stored
├── DroneRepository.java       Spring Data JPA repository
├── DroneController.java       REST endpoints (GET /drones, POST /drones)
├── GlobalExceptionHandler.java   Turns validation errors into structured 400 responses
├── dto/
│   └── DroneDTO.java           The API's request/response shape (validated)
└── mapper/
    └── DroneMapper.java         Converts between DroneDTO and Drone
```

The DTO is kept separate from the entity on purpose: it means the API's public contract can stay stable even if the underlying database model changes, and it keeps validation rules focused on what a client actually sends, rather than mixing them into the persistence layer.

---

## 🧪 Testing

```bash
./mvnw test
```

Includes a `@WebMvcTest` slice test for `DroneController`, verifying that an invalid battery value correctly returns a `400 Bad Request`.

---

## 📦 Requirements

- Java 21
- Maven (or the bundled `./mvnw` wrapper)
- Dependencies: Spring Boot Web, Spring Data JPA, Spring Validation, H2, springdoc-openapi (all resolved automatically by Maven)

---

*A personal project exploring REST API design, validation, and layered architecture (entity/DTO/mapper) with Spring Boot.*
