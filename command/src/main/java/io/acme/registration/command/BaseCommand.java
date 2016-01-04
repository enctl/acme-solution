package io.acme.registration.command;

import java.io.Serializable;
import java.util.UUID;

/**
 * Base command entity
 */
public class BaseCommand implements Serializable {

    private UUID id;

    public BaseCommand() {

    }

    public BaseCommand(final UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
