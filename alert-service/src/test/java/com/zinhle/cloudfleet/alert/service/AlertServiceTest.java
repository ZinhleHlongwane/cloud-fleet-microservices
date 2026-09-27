package com.zinhle.cloudfleet.alert.service;

import com.zinhle.cloudfleet.alert.domain.*;
import com.zinhle.cloudfleet.alert.repository.AlertRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlertServiceTest {
    @Mock AlertRepository repository;
    AlertService service;

    @BeforeEach void setup(){MockitoAnnotations.openMocks(this); service=new AlertService(repository);}

    @Test void acknowledgeMarksAlert() {
        Alert alert=new Alert("BatteryLow","D1",AlertSeverity.WARNING,"battery");
        when(repository.findById(1L)).thenReturn(Optional.of(alert));
        service.acknowledge(1L);
        assertTrue(alert.isAcknowledged());
        assertNotNull(alert.getAcknowledgedAt());
    }
}
