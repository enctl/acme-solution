package io.acme.registration.persistence.test;

import io.acme.registration.domain.Profile;
import io.acme.registration.domain.api.repo.ProfileRepository;
import io.acme.registration.persistence.PersistenceBootstrap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Unit testing for the persistence implementation of the profile repository
 */
@IntegrationTest
@ActiveProfiles("default,test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PersistenceBootstrap.class)
public class ProfileRepositoryImplTest {

    private static final String DATA_USERNAME = "testname";
    private static final String DATA_EMAIL = "test.mail@domain.com";
    private static final String DATA_PASSWORD = "passw0rd";

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    public void testProfileSave() {
        final Profile profile = new Profile(DATA_USERNAME, DATA_EMAIL, DATA_PASSWORD);
        this.profileRepository.save(profile);
        Assert.assertTrue(true);
    }

}
