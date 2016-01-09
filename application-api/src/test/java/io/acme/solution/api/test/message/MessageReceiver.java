package io.acme.solution.api.test.message;

import io.acme.solution.command.RegisterNewUserProfileCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * Utility Rabbit message receiver to consume messages from the queue
 */
public class MessageReceiver {

    private static final Logger log = LoggerFactory.getLogger(MessageReceiver.class);

    public static final String CALLBACK_NAME = "receiveCommandMessage";

    private CountDownLatch latch;
    private RegisterNewUserProfileCommand lastMessage;

    public MessageReceiver() {
        log.info("Command message received");
        this.latch = new CountDownLatch(1);
        this.lastMessage = null;
    }

    public void handleMessage(final RegisterNewUserProfileCommand message) {
        this.lastMessage = message;
        this.latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public RegisterNewUserProfileCommand getLastMessage() {
        return lastMessage;
    }
}
