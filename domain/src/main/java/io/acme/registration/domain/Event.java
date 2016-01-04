package io.acme.registration.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * Domain events resulting from the interaction with the domain entities
 */
public abstract class Event implements Serializable, Comparable<Event> {


    private UUID id;
    private UUID aggregateId;
    private Long version;


    public Event(final UUID aggregateId, final Long version) {
        this.id = UUID.randomUUID();
        this.aggregateId = aggregateId;
        this.version = version;
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

    public abstract Map<String, Object> getEntries();

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Event) && ((Event) obj).getId().equals(this.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public int compareTo(final Event event) {
        int idCompare = this.id.compareTo(event.getId());

        if (idCompare == 0) {
            return (int) (this.version - event.version);
        } else {
            return idCompare;
        }
    }
}
