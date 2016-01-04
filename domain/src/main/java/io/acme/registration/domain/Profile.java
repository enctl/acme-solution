package io.acme.registration.domain;

import io.acme.registration.domain.api.EventHandler;
import io.acme.registration.domain.event.profile.ProfileRegisteredEvent;
import io.acme.registration.domain.util.MementoBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

/**
 * Domain entity for representing the user profiles
 */
public class Profile extends BaseAggregate {

    private static final Logger log = LoggerFactory.getLogger(Profile.class);

    private static final String MEMKEY_USERNAME = "username";
    private static final String MEMKEY_EMAIL = "email";
    private static final String MEMKEY_HASHEDPASS = "hashedPassword";

    private static final String HASHING_ALGORITHM = "SHA-512";

    private String username;
    private String email;
    private String hashedPassword;

    public Profile(final String username, final String email, final String plainPassword) {
        super();
        this.registerHandlers();
        this.apply(new ProfileRegisteredEvent(this.getId(), this.upgrade(), username, email, Profile.hashPassword(plainPassword)));
    }

    public Profile(final UUID id) {
        super(id);
        this.registerHandlers();
    }

    public Profile(final UUID id, final AggregateMemento memento) {
        super(id);
        this.registerHandlers();
        this.restoreFromMemento(memento);
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
    public AggregateMemento saveToMemento() {
        final HashMap<String, Object> attributesMap = new HashMap<>();

        attributesMap.put(MEMKEY_USERNAME, this.username);
        attributesMap.put(MEMKEY_EMAIL, this.email);
        attributesMap.put(MEMKEY_HASHEDPASS, this.hashedPassword);

        return new AggregateMemento(this.getVersion(), MementoBuilder.flatten(attributesMap));
    }

    @Override
    public void restoreFromMemento(AggregateMemento memento) {
        final HashMap<String, Object> attributesMap = MementoBuilder.construct(memento.getState());

        this.setVersion(memento.getVersion());
        this.username = attributesMap.get(MEMKEY_USERNAME).toString();
        this.email = attributesMap.get(MEMKEY_EMAIL).toString();
        this.hashedPassword = attributesMap.get(MEMKEY_HASHEDPASS).toString();
        this.clear();
    }


    //<editor-fold desc="Support Methods">
    /*
     * Register all the supported event handlers to the base aggregate
     */
    private void registerHandlers() {
        this.registerEventHandler(ProfileRegisteredEvent.class, new ProfileRegisteredEventHandler());
    }

    /*
     * Hash the password before saving into the domain entity
     */
    private static String hashPassword(final String plainPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASHING_ALGORITHM);
            digest.reset();
            return Base64.getEncoder().encodeToString(digest.digest(plainPassword.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            log.debug("Couldn't locate the password hasing algorithm on the current platform");
            log.trace("Couldn't find {" + HASHING_ALGORITHM + "}", exception);
            return null;
        }
    }
    //</editor-fold>


    //<editor-fold desc="Event Handlers">
    private class ProfileRegisteredEventHandler implements EventHandler<ProfileRegisteredEvent> {

        @Override
        public void handle(final ProfileRegisteredEvent event) {
            Profile.this.username = event.getUsername();
            Profile.this.email = event.getEmail();
            Profile.this.hashedPassword = event.getHashedPassword();
        }
    }
    //</editor-fold>


}
