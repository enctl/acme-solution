package io.acme.registration.domain;

/**
 * Memento pattern for saving/reconstructing the domain aggregate/entities
 */
public class AggregateMemento {

    private Long version;
    private String state;

    public AggregateMemento(final Long version, final String state) {
        this.version = version;
        this.state = state;
    }

    public Long getVersion() {
        return this.version;
    }

    public String getState() {
        return this.state;
    }

    @Override
    public String toString() {
        return "{version: " + this.version + ", state: " + this.state + "}";
    }
}
