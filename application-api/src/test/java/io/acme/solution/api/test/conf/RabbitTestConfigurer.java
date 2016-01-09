package io.acme.solution.api.test.conf;

import io.acme.solution.api.test.message.MessageReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Rabbit Test Configurer for creating the queues to subscribe to all the commands
 */
@Configuration
public class RabbitTestConfigurer {

    private static final Logger log = LoggerFactory.getLogger(RabbitTestConfigurer.class);

    @Value("${command.bus.rabbit.queue}")
    private String queueName;

    @Bean
    public Queue getCommandsQueue() {
        log.info("Creating a durable exclusive test drain command queue as auto-delete with name: {" + this.queueName + "}");
        return new Queue(this.queueName, true, true, true);
    }

    @Bean
    public Binding getCommandsBinding(final Queue queue, final TopicExchange exchange) {
        log.info("Binding test drain command queue to the test command exchange ");
        return BindingBuilder.bind(queue).to(exchange).with("#");
    }

    @Bean
    public SimpleMessageListenerContainer getMessageListenerContainer(final ConnectionFactory connectionFactory,
                                                                      final Queue commandDrainQueue,
                                                                      final MessageListenerAdapter messageListener) {

        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(commandDrainQueue);
        container.setMessageListener(messageListener);

        return container;
    }

    @Bean
    public MessageReceiver getMessageReceiver() {
        return new MessageReceiver();
    }

    @Bean
    public MessageListenerAdapter getListenerAdapter(final MessageReceiver receiver, final MessageConverter converter) {
        return new MessageListenerAdapter(receiver, converter);
    }
}
