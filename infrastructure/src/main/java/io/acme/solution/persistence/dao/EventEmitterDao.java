package io.acme.solution.persistence.dao;

import io.acme.solution.persistence.dao.model.PersistentEventEmitter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Dao interface for accessing the event emitter entries. It'll be proxied by Spring Data
 */
@Repository
public interface EventEmitterDao extends MongoRepository<PersistentEventEmitter, UUID> {

    public PersistentEventEmitter getById(final UUID id);
}
