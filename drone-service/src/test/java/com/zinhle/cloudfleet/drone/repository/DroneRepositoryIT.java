package com.zinhle.cloudfleet.drone.repository;

import com.zinhle.cloudfleet.drone.domain.Drone;
import com.zinhle.cloudfleet.drone.domain.DroneStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers(disabledWithoutDocker = true)
@DataJpaTest
class DroneRepositoryIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureDatabaseProperties(DynamicPropertyRegistry registry) {
        registry.add(
                "spring.jpa.properties.hibernate.default_schema",
                () -> "drone"
        );
    }

    @Autowired
    DroneRepository repository;

    @Test
    void persistsDroneInRealPostgres() {
        Drone drone = new Drone(
                "IT-1",
                "ITSN-1",
                "Test",
                DroneStatus.AVAILABLE,
                90,
                new BigDecimal("3.0"),
                null,
                null
        );

        repository.saveAndFlush(drone);

        assertTrue(repository.findById("IT-1").isPresent());
    }
}