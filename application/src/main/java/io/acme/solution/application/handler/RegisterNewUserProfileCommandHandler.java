/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Eslam Nawara
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
