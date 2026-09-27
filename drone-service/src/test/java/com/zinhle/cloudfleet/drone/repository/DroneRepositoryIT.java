package com.zinhle.cloudfleet.drone.repository;

import com.zinhle.cloudfleet.drone.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers(disabledWithoutDocker = true)
@DataJpaTest
class DroneRepositoryIT {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired DroneRepository repository;

    @Test void persistsDroneInRealPostgres() {
        Drone drone=new Drone("IT-1","ITSN-1","Test",DroneStatus.AVAILABLE,90,new BigDecimal("3.0"),null,null);
        repository.saveAndFlush(drone);
        assertTrue(repository.findById("IT-1").isPresent());
    }
}
