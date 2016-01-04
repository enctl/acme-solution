package io.acme.registration.command;

import java.util.UUID;

/**
 * Register New User Profile Command.
 */
public class RegisterNewUserProfileCommand extends BaseCommand {

    private String username;
    private String email;

    public RegisterNewUserProfileCommand() {

    }

    public RegisterNewUserProfileCommand(final UUID id, final String username, final String email) {
        super(id);
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "{" + this.getClass().getSimpleName() + " : " + this.getId() + " : " + this.username + " : " + this.email + "}";
    }
}
