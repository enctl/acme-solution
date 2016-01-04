package io.acme.registration.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configurer class to bootstrap the needed artifacts for RabbitMQ
 */
@Configuration
public class RabbitConfigurer {

    private static final Logger log = LoggerFactory.getLogger(RabbitConfigurer.class);

    @Value("${command.bus.rabbit.host}")
    private String hostname;

    @Value("${command.bus.rabbit.port}")
    private Integer port;

    @Value("${command.bus.rabbit.username}")
    private String username;

    @Value("${command.bus.rabbit.password}")
    private String password;

    @Value("${command.bus.rabbit.exchange}")
    private String exchange;

    @Value("${command.bus.rabbit.exchange.deletable}")
    private boolean deletable;

    @Bean
    public MessageConverter getMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public ConnectionFactory getConnectionFactory() {

        log.info("Creating rabbit connection for command bus on host: {" + this.hostname + "}");

        final CachingConnectionFactory connectionFactory = new CachingConnectionFactory(this.hostname, this.port);
        connectionFactory.setUsername(this.username);
        connectionFactory.setPassword(this.password);

        return connectionFactory;
    }

    @Bean
    public AmqpAdmin getRabbitAdmin(final ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public RabbitTemplate getRabbitTemplate(final ConnectionFactory connectionFactory, final MessageConverter messageConverter) {

        log.info("Plugging in custom json marshaller for RabbitMQ");

        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);

        return rabbitTemplate;
    }

    @Bean
    public TopicExchange getCommandExchange() {
        log.info("Creating a durable topic exchange for the command bus with name: {" + this.exchange +
                "} and auto-delete is: {" + this.deletable + "}");

        return new TopicExchange(this.exchange, true, this.deletable);
    }
}
