package com.zinhle.cloudfleet.drone.service;

import com.zinhle.cloudfleet.drone.api.*;
import com.zinhle.cloudfleet.drone.domain.*;
import com.zinhle.cloudfleet.drone.repository.DroneRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DroneServiceTest {
    @Mock DroneRepository repository;
    DroneService service;

    @BeforeEach void setup() {
        MockitoAnnotations.openMocks(this);
        service = new DroneService(repository);
    }

    @Test void createPersistsDrone() {
        DroneRequest request = new DroneRequest("DRN-001","SN-001","Falcon",DroneStatus.AVAILABLE,90,
                new BigDecimal("5.0"),-26.1,28.0);
        when(repository.save(any(Drone.class))).thenAnswer(inv -> inv.getArgument(0));

        DroneResponse response = service.create(request);

        assertEquals("DRN-001", response.droneId());
        verify(repository).save(any(Drone.class));
    }

    @Test void duplicateIdIsRejected() {
        when(repository.existsById("DRN-001")).thenReturn(true);
        DroneRequest request = new DroneRequest("DRN-001","SN-001","Falcon",DroneStatus.AVAILABLE,90,
                new BigDecimal("5.0"),null,null);
        assertThrows(ConflictException.class, () -> service.create(request));
    }

    @Test void missingDroneReturns404StyleException() {
        when(repository.findById("missing")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById("missing"));
    }

    @Test void updateChangesEditableFields() {
        Drone drone = new Drone("D1","S1","Old",DroneStatus.AVAILABLE,80,new BigDecimal("2"),null,null);
        when(repository.findById("D1")).thenReturn(Optional.of(drone));
        DroneResponse result = service.update("D1", new DroneUpdateRequest("New",DroneStatus.MAINTENANCE,40,
                new BigDecimal("3"),1.0,2.0));
        assertEquals("New", result.model());
        assertEquals(DroneStatus.MAINTENANCE, result.status());
        assertEquals(40, result.batteryLevel());
    }

    @Test void deleteDecommissionsInsteadOfHardDeleting() {
        Drone drone = new Drone("D1","S1","Model",DroneStatus.AVAILABLE,80,new BigDecimal("2"),null,null);
        when(repository.findById("D1")).thenReturn(Optional.of(drone));
        service.delete("D1");
        assertEquals(DroneStatus.DECOMMISSIONED, drone.getStatus());
        verify(repository, never()).delete(any());
    }
}
