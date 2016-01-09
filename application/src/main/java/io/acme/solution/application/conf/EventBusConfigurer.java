package io.acme.solution.application.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Spring boot configuration class for registering the queues on Rabbit for event bus
 */
@Configuration
public class EventBusConfigurer {

    private static final Logger log = LoggerFactory.getLogger(CommandBusConfigurer.class);

    @Value("${event.bus.rabbit.host}")
    private String hostname;

    @Value("${event.bus.rabbit.port}")
    private Integer port;

    @Value("${event.bus.rabbit.username}")
    private String username;

    @Value("${event.bus.rabbit.password}")
    private String password;

    @Value("${event.bus.rabbit.exchange}")
    private String exchange;


    @Bean
    public MessageConverter eventBusMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    @Primary
    public ConnectionFactory eventBusConnectionFactory() {

        log.info("Creating event bus connection on hostname: {" + this.hostname + "}");

        final CachingConnectionFactory connectionFactory = new CachingConnectionFactory(this.hostname, this.port);
        connectionFactory.setUsername(this.username);
        connectionFactory.setPassword(this.password);

        return connectionFactory;
    }

    @Bean
    @Autowired
    @Primary
    public RabbitAdmin eventBusRabbitAdmin(final ConnectionFactory eventBusConnectionFactory) {

        log.info("Creating event bus RabbitMQ administrator template");
        return new RabbitAdmin(eventBusConnectionFactory);
    }

    @Bean
    @Autowired
    @Primary
    public RabbitTemplate eventBusRabbitTemplate(final ConnectionFactory eventBusConnectionFactory, final MessageConverter eventBusMessageConverter) {

        log.info("Creating event bus RabbitMQ template with a custom JSON converter");
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(eventBusConnectionFactory);
        rabbitTemplate.setMessageConverter(eventBusMessageConverter);

        return rabbitTemplate;
    }

    @Bean
    public TopicExchange eventExchange() {

        log.info("Creating event bus topic exchange with the name : {" + this.exchange + "}");
        return new TopicExchange(this.exchange, true, false);
    }

}
