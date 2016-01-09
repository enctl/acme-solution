package io.acme.solution.api.bundle;

/**
 * Validation messages resource bundle keys
 */
public final class MessageKeys {

    public static class UserProfile {

        public static final String EMPTY_EMAIL = "user.profile.field.email.empty";
        public static final String EMPTY_USERNAME = "user.profile.field.username.empty";
        public static final String INVALID_EMAIL = "user.profile.field.email.invalid";
        public static final String INVALID_USERNAME = "user.profile.field.username.invalid";

        private UserProfile() {

        }
    }


    private MessageKeys() {

    }
}
