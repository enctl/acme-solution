package io.acme.solution.domain.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;

/**
 * A builder class for creating/reading mementos
 */
public class MementoBuilder {

    private static final Logger log = LoggerFactory.getLogger(MementoBuilder.class);

    public static String flatten(final HashMap<String, Object> attributesMap) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(attributesMap);
        } catch (JsonProcessingException exception) {
            log.debug("Failed to craete memento object");
            log.trace("Failed to create memento object for the passed attributes map", exception);
            return null;
        }
    }

    public static HashMap<String, Object> construct(final String state) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return (HashMap<String, Object>) mapper.readValue(state, HashMap.class);
        } catch (IOException exception) {
            log.debug("Failed to reconstruct memento object");
            log.trace("Failed to reconstruct the memento attributes map", exception);
            return null;
        }
    }
}
