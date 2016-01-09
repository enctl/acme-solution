package io.acme.solution.domain;

import io.acme.solution.domain.api.EventEmitter;
import io.acme.solution.domain.api.EventHandler;

import java.util.*;

/**
 * The abstract base for any event-based aggregate/entity in the domain model
 */
public abstract class BaseAggregate implements EventEmitter, Comparable<BaseAggregate> {

    protected static final String MEMKEY_VERSION = "version";

    private UUID id;
    private Long version;
    private Set<Event> deltaEvents;
    private Map<Class<? extends Event>, EventHandler> handlerRegistry;

    public BaseAggregate() {
        this.id = UUID.randomUUID();
        this.version = 0L;
        this.deltaEvents = new TreeSet<>();
        this.handlerRegistry = new HashMap<>();
    }

    public BaseAggregate(final UUID id) {
        this();
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    @Override
    public Set<Event> getChangeLog() {
        Set<Event> clonedEvents = new TreeSet<Event>();
        clonedEvents.addAll(this.deltaEvents);

        return clonedEvents;
    }

    @Override
    public <T extends Event> void apply(final T event) {
        final EventHandler<T> handler = this.handlerRegistry.get(event.getClass());

        if (handler != null) {
            handler.handle(event);
            this.setVersion(event.getVersion());
            this.deltaEvents.add(event);
        }
    }

    @Override
    public <T extends Event> void replay(List<T> eventsList) {
        Set<Event> upcomingEvents = new TreeSet<>();
        upcomingEvents.addAll(eventsList);

        for (Event current : upcomingEvents) {
            this.apply(current);
        }
    }

    @Override
    public void clear() {
        this.deltaEvents.clear();
    }

    @Override
    public int compareTo(final BaseAggregate aggregate) {
        int idCompare = this.id.compareTo(aggregate.getId());

        if (idCompare == 0) {
            return (int) (this.version - aggregate.version);
        } else {
            return idCompare;
        }
    }

    protected Long upgrade() {
        return ++this.version;
    }

    protected void setVersion(final Long version) {
        this.version = version;
    }

    protected <T extends Event> void registerEventHandler(Class<T> clazz, EventHandler<T> handler) {
        this.handlerRegistry.put(clazz, handler);
    }
}
