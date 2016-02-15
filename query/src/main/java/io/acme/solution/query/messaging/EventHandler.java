package io.acme.solution.query.messaging;

import java.util.Map;

/**
 * Query side event handler
 */
public interface EventHandler {

    public static final String MEMKEY_ID = "id";
    public static final String MEMKEY_AGGREGATE_ID = "aggregateId";
    public static final String MEMKEY_VERSION = "version";

    public void handleMessage(final Map<String, Object> eventEntries);

    public String getInterest();
}
