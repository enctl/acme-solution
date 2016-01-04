package io.acme.registration.domain.event.profile;

import io.acme.registration.domain.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Domain event for user registration
 */
public class ProfileRegisteredEvent extends Event {

    public static final String MAPKEY_USERNAME = "username";
    public static final String MAPKEY_EMAIL = "email";
    public static final String MAPKEY_HASHEDPASS = "hashedPassword";

    private String username;
    private String email;
    private String hashedPassword;

    /**
     * @param aggregateId the identification of the domain aggregate/entity that generated that event
     * @param version     the version of the aggregated AFTER the event has been applied
     */
    public ProfileRegisteredEvent(final UUID aggregateId, final Long version, final String username, final String email,
                                  final String hashedPassword) {

        super(aggregateId, version);
        this.username = username;
        this.email = email;
        this.hashedPassword = hashedPassword;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getHashedPassword() {
        return this.hashedPassword;
    }

    @Override
    public Map<String, Object> getEntries() {
        final Map<String, Object> eventEntries = new HashMap<>();

        eventEntries.put(MAPKEY_USERNAME, this.username);
        eventEntries.put(MAPKEY_EMAIL, this.email);
        eventEntries.put(MAPKEY_HASHEDPASS, this.hashedPassword);

        return eventEntries;
    }
}
