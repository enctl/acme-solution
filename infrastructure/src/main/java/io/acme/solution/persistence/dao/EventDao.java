package io.acme.solution.persistence.dao;

import io.acme.solution.persistence.dao.model.PersistentEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Dao interface for accessing the events entries. It'll be proxied by Spring Data
 */
@Repository
public interface EventDao extends MongoRepository<PersistentEvent, UUID> {

    public PersistentEvent getById(final UUID id);
}
