package io.acme.solution.persistence.dao.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A persistent object for storing the domain entities generated events
 */
@Document(collection = "events")
public class PersistentEvent implements Comparable<PersistentEvent> {

    @Id
    private UUID id;
    private UUID aggregateId;
    private Long version;
    private String eventType;
    private Map<String, Object> entries;


    public PersistentEvent() {

    }

    public PersistentEvent(final UUID id, final UUID aggregateId, final Long version, final String eventType, final Map<String, Object> entries) {
        this.id = id;
        this.aggregateId = aggregateId;
        this.version = version;
        this.eventType = eventType;
        this.entries = new HashMap<>(entries);
    }

    public UUID getId() {
        return id;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public Long getVersion() {
        return version;
    }

    public String getEventType() {
        return eventType;
    }

    public Map<String, Object> getEntries() {
        return new HashMap<>(this.entries);
    }


    @Override
    public boolean equals(Object obj) {
        return (obj instanceof PersistentEvent) && ((PersistentEvent) obj).getId().equals(this.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public int compareTo(final PersistentEvent event) {
        int idCompare = this.id.compareTo(event.getId());

        if (idCompare == 0) {
            return (int) (this.version - event.version);
        } else {
            return idCompare;
        }
    }

    @Override
    public String toString() {
        return String.format("{%s: id: %s, aggregateId: %s, version: %s, entries: %s}", this.getClass().getSimpleName(),
                this.id, this.version, this.entries);
    }
}
