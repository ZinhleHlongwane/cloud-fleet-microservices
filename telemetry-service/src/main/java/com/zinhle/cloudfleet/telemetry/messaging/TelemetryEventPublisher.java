package com.zinhle.cloudfleet.telemetry.messaging;

import com.zinhle.cloudfleet.events.FleetEvent;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class TelemetryEventPublisher {
    private final JmsTemplate jmsTemplate;
    public TelemetryEventPublisher(JmsTemplate jmsTemplate){this.jmsTemplate=jmsTemplate;}
    public void publish(FleetEvent event){jmsTemplate.convertAndSend("fleet.events.queue", event);}
}
