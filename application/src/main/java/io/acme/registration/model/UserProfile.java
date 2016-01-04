package io.acme.registration.model;

/**
 * A view model for containing the user profile information.
 */
public class UserProfile {

    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_EMAIL = "email";

    private String username;
    private String email;

    public UserProfile() {
        this.username = "";
        this.email = "";
    }

    public UserProfile(final String username, final String email) {
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
        return "{" + this.getClass().getSimpleName() + " : " + this.username + " : " + this.email + "}";
    }
}
