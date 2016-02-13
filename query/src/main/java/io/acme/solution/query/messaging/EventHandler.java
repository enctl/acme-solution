package io.acme.solution.query.messaging;

import java.util.Map;

/**
 * Query side event handler
 */
public interface EventHandler {

    public void handleMessage(final Map<String, Object> eventEntries);

    public String getInterest();
}
