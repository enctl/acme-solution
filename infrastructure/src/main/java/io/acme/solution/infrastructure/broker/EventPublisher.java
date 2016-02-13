package io.acme.solution.infrastructure.broker;

import io.acme.solution.infrastructure.dao.model.PersistentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Domain events publisher for the interested parties
 */
@Component
public class EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(EventPublisher.class);

    @Value("${event.bus.rabbit.exchange}")
    private String exchange;

    @Autowired
    private RabbitTemplate eventBusRabbitTemplate;

    public void publish(final Set<PersistentEvent> eventSet) {

        for (PersistentEvent currentEvent : eventSet) {
            this.eventBusRabbitTemplate.convertAndSend(this.exchange, currentEvent.getEventType(), currentEvent.getEntries());
        }
    }
}
