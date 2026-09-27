package com.zinhle.cloudfleet.telemetry.service;

import com.zinhle.cloudfleet.telemetry.api.TelemetryRequest;
import com.zinhle.cloudfleet.telemetry.domain.TelemetrySample;
import com.zinhle.cloudfleet.telemetry.messaging.TelemetryEventPublisher;
import com.zinhle.cloudfleet.telemetry.repository.TelemetryRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;

import static org.mockito.Mockito.*;
import java.time.Instant;

class TelemetryServiceTest {
    @Mock TelemetryRepository repository;
    @Mock TelemetryEventPublisher publisher;
    TelemetryService service;

    @BeforeEach void setup(){
        MockitoAnnotations.openMocks(this);
        service=new TelemetryService(repository,publisher);
        when(repository.save(any(TelemetrySample.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test void lowBatteryPublishesEvent(){
        service.record(new TelemetryRequest("D1",15,-26.1,28.0,50.0,30.0, Instant.now()));
        verify(publisher).publish(any());
    }

    @Test void healthyBatteryDoesNotPublishLowBatteryEvent(){
        service.record(new TelemetryRequest("D1",80,-26.1,28.0,50.0,30.0, Instant.now()));
        verify(publisher, never()).publish(argThat(e -> e != null && "BatteryLow".equals(e.eventType())));
    }
}
