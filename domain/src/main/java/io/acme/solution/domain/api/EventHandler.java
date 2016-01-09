package io.acme.solution.domain.api;

import io.acme.solution.domain.Event;

/**
 * Domain event handler interface
 */
public interface EventHandler<T extends Event> {

    public void handle(final T event);
}
