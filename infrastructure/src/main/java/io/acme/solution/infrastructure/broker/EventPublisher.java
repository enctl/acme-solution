/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Eslam Nawara
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
