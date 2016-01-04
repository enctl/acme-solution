package io.acme.registration.model;

import java.util.UUID;

/**
 * A generic response by the application layer when the client submits a command for execution
 */
public class CommandPromise {

    private UUID id;
    private String message;


    public CommandPromise() {

    }

    public CommandPromise(final UUID id, final String message) {
        this.id = id;
        this.message = message;
    }

    public UUID getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof CommandPromise) && ((CommandPromise) obj).getId().equals(this.id);
    }

    @Override
    public String toString() {
        return "{" + this.getClass().getSimpleName() + " : " + this.id + " : " + this.message + "}";
    }
}
