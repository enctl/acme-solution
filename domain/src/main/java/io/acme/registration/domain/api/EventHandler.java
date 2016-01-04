package io.acme.registration.domain.api;

import io.acme.registration.domain.Event;

/**
 * Domain event handler interface
 */
public interface EventHandler<T extends Event> {

    public void handle(final T event);
}
