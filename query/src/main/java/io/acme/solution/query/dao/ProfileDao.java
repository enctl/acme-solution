package io.acme.solution.query.dao;

import io.acme.solution.query.model.QueryableProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring data implemented interface
 */
@Repository
public interface ProfileDao extends MongoRepository<QueryableProfile, UUID> {

    public QueryableProfile findByUsername(final String username);

    public QueryableProfile findByEmail(final String email);
}
