package io.acme.solution.infrastructure.repo;

import io.acme.solution.domain.Event;
import io.acme.solution.domain.Profile;
import io.acme.solution.domain.api.repo.ProfileRepository;
import io.acme.solution.infrastructure.broker.EventPublisher;
import io.acme.solution.infrastructure.dao.EventDao;
import io.acme.solution.infrastructure.dao.EventEmitterDao;
import io.acme.solution.infrastructure.dao.SnapshotDao;
import io.acme.solution.infrastructure.dao.model.PersistentEvent;
import io.acme.solution.infrastructure.dao.model.PersistentEventEmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * NoSQL implementation for profile repository
 */
@Component
public class ProfileRepositoryImpl implements ProfileRepository {

    private static final Logger log = LoggerFactory.getLogger(ProfileRepositoryImpl.class);

    @Autowired
    private EventEmitterDao eventEmitterDao;

    @Autowired
    private EventDao eventDao;

    @Autowired
    private SnapshotDao snapshotDao;

    @Autowired
    private EventPublisher eventPublisher;

    @Value("${persistence.db.snapshot.bucket}")
    private Integer snapshotBucket;

    @Override
    public Profile get(final UUID id) {
        return null;
    }

    @Override
    public void save(final Profile profile) {

        log.info("Saving the profile");

        PersistentEventEmitter persistentEventEmitter = this.eventEmitterDao.getById(profile.getId());
        Set<PersistentEvent> persistentEventSet = new HashSet<>();
        Set<Event> eventSet = null;

        if (persistentEventEmitter == null) {
            persistentEventEmitter = new PersistentEventEmitter(profile.getId(), profile.getVersion(),
                    profile.getClass().getSimpleName());
        } else {
            persistentEventEmitter.setVersion(profile.getVersion());
        }

        eventSet = profile.getChangeLog();

        persistentEventSet.addAll(eventSet.stream().map(currentEvent -> new PersistentEvent(currentEvent.getId(),
                currentEvent.getAggregateId(), currentEvent.getVersion(), currentEvent.getClass().getSimpleName(),
                currentEvent.getEntries())).collect(Collectors.toList()));

        this.eventEmitterDao.save(persistentEventEmitter);
        this.eventDao.save(persistentEventSet);
        this.eventPublisher.publish(persistentEventSet);

        profile.clear();

        //TODO Add Snapshot Logic
    }

    @Override
    public boolean exists(final UUID id) {
        return false;
    }

    @Override
    public boolean exists(final String username) {
        return false;
    }
}
