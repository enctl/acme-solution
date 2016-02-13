package io.acme.solution.application.handler;

import io.acme.solution.application.messaging.CommandHandler;
import io.acme.solution.command.Command;
import io.acme.solution.command.RegisterNewUserProfileCommand;
import io.acme.solution.domain.Profile;
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
		if (command instanceof RegisterNewUserProfileCommand) {

			final RegisterNewUserProfileCommand registrationCommand = (RegisterNewUserProfileCommand) command;
			final Profile userProfile = new Profile(registrationCommand.getId(), registrationCommand.getUsername(),
					registrationCommand.getEmail(), registrationCommand.getPassword());

			this.profileRepository.save(userProfile);
		} else {
			log.error("RegisterNewUserProfileCommandHandler received an invalid command type");
		}
	}

	@Override
	public Class<? extends Command> getInterest() {
		return RegisterNewUserProfileCommand.class;
	}
}
