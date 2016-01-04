package io.acme.registration.persistence.dao.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

/**
 * A persistent object for storing the domain entities aggregates
 */
@Document(collection = "emitters")
public class PersistentEventEmitter implements Comparable<PersistentEventEmitter> {

    @Id
    private UUID id;
    private Long version;
    private String emitterType;

    public PersistentEventEmitter() {

    }

    public PersistentEventEmitter(final UUID id, final Long version, final String emitterType) {
        this.id = id;
        this.version = version;
        this.emitterType = emitterType;
    }

    public UUID getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }

    public String getEmitterType() {
        return emitterType;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof PersistentEventEmitter) && ((PersistentEventEmitter) obj).getId().equals(this.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public int compareTo(final PersistentEventEmitter eventEmitter) {
        int idCompare = this.id.compareTo(eventEmitter.getId());

        if (idCompare == 0) {
            return (int) (this.version - eventEmitter.version);
        } else {
            return idCompare;
        }
    }

    @Override
    public String toString() {
        return String.format("{%s: id: %s, version: %s, type: %s}", this.getClass().getSimpleName(),
                this.id, this.version, this.emitterType);
    }
}
