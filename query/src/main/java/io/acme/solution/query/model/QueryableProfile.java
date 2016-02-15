package io.acme.solution.query.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

/**
 * Data model for querying profile
 */
@Document(collection = "profiles")
public class QueryableProfile {

    @Id
    private UUID id;
    private Long version;
    private String username;
    private String email;

    public QueryableProfile() {

    }

    public QueryableProfile(final UUID id, final Long version, final String username, final String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Long getVersion() {
        return version;
    }
}
