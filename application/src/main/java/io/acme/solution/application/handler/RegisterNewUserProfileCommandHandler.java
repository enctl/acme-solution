package io.acme.solution.application.handler;

import io.acme.solution.application.messaging.CommandHandler;
import io.acme.solution.command.Command;
import io.acme.solution.command.RegisterNewUserProfileCommand;
import io.acme.solution.domain.api.repo.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Command handler for the new user registration command
 */
public class RegisterNewUserProfileCommandHandler implements CommandHandler {

    private static final Logger log = LoggerFactory.getLogger(RegisterNewUserProfileCommandHandler.class);

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public void handleMessage(final Command command) {
        log.info("Triggered for incoming command : " + command);
        log.info("Command handler injection status is: " + profileRepository);
    }

    @Override
    public Class<? extends Command> getInterest() {
        return RegisterNewUserProfileCommand.class;
    }
}
