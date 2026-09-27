package com.zinhle.cloudfleet.maintenance.messaging;

import com.zinhle.cloudfleet.events.FleetEvent;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceEventPublisher {
    private final JmsTemplate jmsTemplate;
    public MaintenanceEventPublisher(JmsTemplate jmsTemplate){this.jmsTemplate=jmsTemplate;}
    public void publish(FleetEvent event){jmsTemplate.convertAndSend("fleet.events.queue", event);}
}
