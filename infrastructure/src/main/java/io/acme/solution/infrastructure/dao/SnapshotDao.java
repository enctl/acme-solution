package io.acme.solution.infrastructure.dao;

import io.acme.solution.infrastructure.dao.model.PersistentSnapshot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Dao interface for accessing the snapshot entries. It'll be proxied by Spring Data
 */
@Repository
public interface SnapshotDao extends MongoRepository<PersistentSnapshot, UUID> {

    public PersistentSnapshot getById(final UUID id);
}
