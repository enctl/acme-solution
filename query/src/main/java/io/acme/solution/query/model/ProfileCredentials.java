package io.acme.solution.query.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

/**
 * Separate persistence entity for the hashed passwords of the registered users
 */
@Document(collection = "profiles-credentials")
public class ProfileCredentials {

    @Id
    private UUID profileId;
    private String hashedPassword;

    public ProfileCredentials() {

    }

    public ProfileCredentials(final UUID profileId, final String hashedPassword) {
        this.profileId = profileId;
        this.hashedPassword = hashedPassword;
    }

    public UUID getProfileId() {
        return profileId;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
}
