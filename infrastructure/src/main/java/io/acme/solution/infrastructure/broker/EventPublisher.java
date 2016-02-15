package io.acme.solution.infrastructure.broker;

import io.acme.solution.infrastructure.dao.model.PersistentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Domain events publisher for the interested parties
 */
@Component
public class EventPublisher {

    private static final String MEMKEY_ID = "id";
    private static final String MEMKEY_AGGREGATE_ID = "aggregateId";
    private static final String MEMKEY_VERSION = "version";

    private static final Logger log = LoggerFactory.getLogger(EventPublisher.class);

    @Value("${event.bus.rabbit.exchange}")
    private String exchange;

    @Autowired
    private RabbitTemplate eventBusRabbitTemplate;

    public void publish(final Set<PersistentEvent> eventSet) {

        Map<String, Object> currentEntires = null;

        for (PersistentEvent currentEvent : eventSet) {
            currentEntires = new HashMap<>(currentEvent.getEntries());

            currentEntires.put(MEMKEY_VERSION, currentEvent.getVersion());
            currentEntires.put(MEMKEY_ID, currentEvent.getId());
            currentEntires.put(MEMKEY_AGGREGATE_ID, currentEvent.getAggregateId());

            this.eventBusRabbitTemplate.convertAndSend(this.exchange, currentEvent.getEventType(), currentEntires);
        }
    }
}
