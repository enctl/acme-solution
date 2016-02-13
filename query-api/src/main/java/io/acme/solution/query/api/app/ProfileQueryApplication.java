package io.acme.solution.query.api.app;

import io.acme.solution.query.api.bundle.RESTKeys;
import io.acme.solution.query.model.QueryableProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the query layer responsible for handling the profile queries
 */
@RestController
@RequestMapping(RESTKeys.Profile.BASE)
public class ProfileQueryApplication {

    private static final Logger log = LoggerFactory.getLogger(ProfileQueryApplication.class);


    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<QueryableProfile> getAllProfiles() {
        return new ArrayList<>();
    }
}
