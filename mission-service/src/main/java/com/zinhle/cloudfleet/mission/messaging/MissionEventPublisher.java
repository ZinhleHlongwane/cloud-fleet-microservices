package com.zinhle.cloudfleet.mission.messaging;

import com.zinhle.cloudfleet.events.FleetEvent;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MissionEventPublisher {
    private final JmsTemplate jmsTemplate;

    public MissionEventPublisher(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void publish(FleetEvent event) {
        jmsTemplate.convertAndSend("fleet.events.queue", event);
    }
}
