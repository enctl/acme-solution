package io.acme.registration.domain;

import io.acme.registration.domain.event.profile.ProfileRegisteredEvent;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Unit testing for the domain entity named profile
 */
public class ProfileTest {

    private static final Logger log = LoggerFactory.getLogger(ProfileTest.class);

    private static final String DATA_USERNAME = "testname";
    private static final String DATA_EMAIL = "test.mail@domain.com";
    private static final String DATA_PASSWORD = "passw0rd";

    @Test
    public void testProfileRegistration() {
        final Profile profile = new Profile(DATA_USERNAME, DATA_EMAIL, DATA_PASSWORD);
        final Set<Event> events = profile.getChangeLog();

        Assert.assertNotNull(profile);
        Assert.assertNotNull(events);
        Assert.assertEquals(1, events.size());
        Assert.assertEquals(1L, profile.getVersion().longValue());

        final Event registrationEvent = events.iterator().next();

        Assert.assertNotNull(registrationEvent);
        Assert.assertEquals(1L, registrationEvent.getVersion().longValue());
        Assert.assertEquals(profile.getId(), registrationEvent.getAggregateId());
        Assert.assertEquals(ProfileRegisteredEvent.class, registrationEvent.getClass());
        Assert.assertEquals(DATA_USERNAME, ((ProfileRegisteredEvent) registrationEvent).getUsername());
        Assert.assertEquals(DATA_EMAIL, ((ProfileRegisteredEvent) registrationEvent).getEmail());
        Assert.assertNotEquals(DATA_PASSWORD, ((ProfileRegisteredEvent) registrationEvent).getHashedPassword());
    }

    @Test
    public void testMemento() {
        final Profile profile = new Profile(DATA_USERNAME, DATA_EMAIL, DATA_PASSWORD);
        AggregateMemento memento = profile.saveToMemento();

        Assert.assertNotNull(memento);
        Assert.assertEquals(1L, memento.getVersion().longValue());
        Assert.assertNotNull(memento.getState());

        log.info("Generated memento for profile v1.0 " + memento);

        profile.restoreFromMemento(memento);

        Assert.assertEquals(1L, profile.getVersion().longValue());
        Assert.assertEquals(DATA_USERNAME, profile.getUsername());
        Assert.assertEquals(DATA_EMAIL, profile.getEmail());
        Assert.assertNotEquals(DATA_PASSWORD, profile.getHashedPassword());
    }
}