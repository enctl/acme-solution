package io.acme.solution.api.model;

/**
 * A view model for containing the user profile information.
 */
public class UserProfile {

    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_EMAIL = "email";

    private String username;
    private String email;
    private String password;

    public UserProfile() {
        this.username = "";
        this.email = "";
        this.password = "";
    }

    public UserProfile(final String username, final String email, final String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "{" + this.getClass().getSimpleName() + " : " + this.username + " : " + this.email + "}";
    }
}
