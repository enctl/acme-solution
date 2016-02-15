package io.acme.solution.query.dao;

import io.acme.solution.query.model.ProfileCredentials;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring data implementation
 */
@Repository
public interface ProfileCredentialsDao extends MongoRepository<ProfileCredentials, UUID> {

}
