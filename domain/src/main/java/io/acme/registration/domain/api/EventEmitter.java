package io.acme.registration.domain.api;

import io.acme.registration.domain.AggregateMemento;
import io.acme.registration.domain.Event;

import java.util.List;
import java.util.Set;

/**
 * It's the responsibility interface for any entity/aggregate that has an event generation capabilities.
 */
public interface EventEmitter {

    public Set<Event> getChangeLog();

    public AggregateMemento saveToMemento();

    public void restoreFromMemento(final AggregateMemento memento);

    public <T extends Event> void apply(final T event);

    public <T extends Event> void replay(final List<T> eventsList);

    public void clear();
}
