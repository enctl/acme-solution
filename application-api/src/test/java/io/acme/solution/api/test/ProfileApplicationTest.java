package io.acme.solution.api.test;

import io.acme.solution.api.ApplicationApiBootstrap;
import io.acme.solution.api.bundle.RESTKeys;
import io.acme.solution.api.model.CommandPromise;
import io.acme.solution.api.model.UserProfile;
import io.acme.solution.api.test.message.MessageReceiver;
import io.acme.solution.command.RegisterNewUserProfileCommand;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Spring boot test application
 */
@IntegrationTest
@WebAppConfiguration
@ActiveProfiles("default,test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationApiBootstrap.class)
public class ProfileApplicationTest {

    private static final Logger log = LoggerFactory.getLogger(ProfileApplicationTest.class);

    @Value("${test.server.hostname}")
    private String hostname;

    @Value("${test.server.port}")
    private Integer port;

    @Autowired
    private ApplicationContext context;

    private URL base;
    private RestTemplate restTemplate;

    @Before
    public void setup() {

        try {
            this.base = new URL("http://" + this.hostname + ":" + this.port + "/");
            this.restTemplate = new TestRestTemplate();

        } catch (MalformedURLException exception) {
            log.error("Could not open a test server socket based on the hostname and port provided");
            log.trace("Test server socket couldn't be formed", exception);
            Assert.fail();
        }
    }

    @Test
    public void testProfileRegistration() {

        try {
            final URL profileAppURL = new URL(this.base, RESTKeys.Profile.BASE + RESTKeys.Profile.REGISTRATION);
            final UserProfile testProfile = new UserProfile("testname", "test@email.com", "password");
            final MessageReceiver receiver = this.context.getBean(MessageReceiver.class);
            final ResponseEntity<CommandPromise> responseEntity = this.restTemplate.postForEntity(profileAppURL.toURI(),
                    testProfile, CommandPromise.class);

            Assert.assertThat(responseEntity.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
            Assert.assertThat(responseEntity.getBody().getId(), Matchers.notNullValue());

            receiver.getLatch().await(1000, TimeUnit.MILLISECONDS);

            final RegisterNewUserProfileCommand publishedCommand = receiver.getLastMessage();

            Assert.assertThat(publishedCommand, Matchers.instanceOf(RegisterNewUserProfileCommand.class));
            Assert.assertThat(publishedCommand.getUsername(), Matchers.equalTo("testname"));
            Assert.assertThat(publishedCommand.getEmail(), Matchers.equalTo("test@email.com"));

        } catch (MalformedURLException exception) {
            log.error("Could not open a test server socket for profile solution");
            log.trace("Test profile solution URL is malformed", exception);
            Assert.fail();
        } catch (URISyntaxException exception) {
            log.error("Could not hit profile solution endpoint");
            log.trace("Test profile solution URI is malformed", exception);
            Assert.fail();
        } catch (InterruptedException exception) {
            log.error("Could get the published command from the bus");
            log.trace("Waiting for command to get published is timeout", exception);
            Assert.fail();
        }

    }
}
