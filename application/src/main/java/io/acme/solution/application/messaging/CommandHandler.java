package io.acme.solution.application.messaging;

import io.acme.solution.command.Command;

/**
 * Base interface for handling commands that are published on the command bus.
 */
public interface CommandHandler {

    public void handleMessage(final Command command);

    public Class<? extends Command> getInterest();
}
