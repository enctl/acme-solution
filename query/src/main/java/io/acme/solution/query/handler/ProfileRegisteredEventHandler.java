package io.acme.solution.query.handler;

import io.acme.solution.query.dao.ProfileDao;
import io.acme.solution.query.messaging.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Profile registered event handler
 */
public class ProfileRegisteredEventHandler implements EventHandler {

    private static final String INTEREST = "ProfileRegisteredEvent";
    private static final Logger log = LoggerFactory.getLogger(ProfileRegisteredEventHandler.class);

    @Autowired
    private ProfileDao profileDao;

    @Override
    public void handleMessage(Map<String, Object> eventEntries) {
        log.info("Received repository event" + eventEntries);
        log.info("Inject repository is" + this.profileDao);
    }

    @Override
    public String getInterest() {
        return INTEREST;
    }
}
