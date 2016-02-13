package io.acme.solution.infrastructure.test;

import io.acme.solution.domain.Event;
import io.acme.solution.domain.Profile;
import io.acme.solution.domain.api.repo.ProfileRepository;
import io.acme.solution.infrastructure.InfrastructureContext;
import io.acme.solution.infrastructure.dao.EventDao;
import io.acme.solution.infrastructure.dao.EventEmitterDao;
import io.acme.solution.infrastructure.dao.model.PersistentEvent;
import io.acme.solution.infrastructure.dao.model.PersistentEventEmitter;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;
import java.util.UUID;

/**
 * Unit test application for the profile repository
 */
@ActiveProfiles("default,test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = InfrastructureContext.class)
public class ProfileRepositoryTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private EventEmitterDao eventEmitterDao;

    @Autowired
    private EventDao eventDao;

    @Before
    public void setup() {
        Assert.assertThat(this.profileRepository, Matchers.notNullValue());
    }

    @Test
    public void testSaveProfile() {
        final Profile profile = new Profile(UUID.randomUUID(), "username", "email@domain.com", "password");
        final Set<Event> changeSet = profile.getChangeLog();
        PersistentEvent currentPersistentEvent = null;

        this.profileRepository.save(profile);

        PersistentEventEmitter persistentEventEmitter = this.eventEmitterDao.getById(profile.getId());

        Assert.assertThat(persistentEventEmitter, Matchers.notNullValue());
        Assert.assertThat(persistentEventEmitter.getVersion(), Matchers.equalTo(profile.getVersion()));

        for (Event event : changeSet) {
            currentPersistentEvent = this.eventDao.getById(event.getId());

            Assert.assertThat(currentPersistentEvent, Matchers.notNullValue());
            Assert.assertThat(currentPersistentEvent.getVersion(), Matchers.equalTo(event.getVersion()));
        }
    }
}
