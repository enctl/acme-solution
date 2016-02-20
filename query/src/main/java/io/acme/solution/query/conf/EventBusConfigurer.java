/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Eslam Nawara
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.acme.solution.query.conf;

import io.acme.solution.query.messaging.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Rabbit MQ bus configurer
 */
@Configuration
public class EventBusConfigurer {

    private static final Logger log = LoggerFactory.getLogger(EventBusConfigurer.class);

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

    @Value("${event.handler.package.base}")
    private String handlerBasePackage;

    @Value("${event.bus.rabbit.queue.prefix}")
    private String queuePrefix;

    @Autowired
    private ApplicationContext context;

    @PostConstruct
    private void setup() {

        Queue currentQueue = null;
        SimpleMessageListenerContainer currentContainer = null;
        MessageListenerAdapter currentAdapter = null;

        final RabbitAdmin rabbitAdmin = this.context.getBean("eventBusRabbitAdmin", RabbitAdmin.class);
        final ConnectionFactory connectionFactory = this.context.getBean("eventBusConnectionFactory", ConnectionFactory.class);
        final TopicExchange exchange = this.context.getBean("eventExchange", TopicExchange.class);
        final MessageConverter converter = this.context.getBean("eventBusMessageConverter", MessageConverter.class);
        final Map<String, EventHandler> eventHandlersRegistry = EventHandlerUtils.buildEventHandlersRegistry(this.handlerBasePackage, this.context);

        for (String eventType : eventHandlersRegistry.keySet()) {

            rabbitAdmin.declareQueue(currentQueue = new Queue(this.queuePrefix + eventType));
            rabbitAdmin.declareBinding(BindingBuilder.bind(currentQueue).to(exchange).with(eventType));


            currentAdapter = new MessageListenerAdapter(eventHandlersRegistry.get(eventType), converter);

            currentContainer = new SimpleMessageListenerContainer(connectionFactory);
            currentContainer.setMessageListener(currentAdapter);
            currentContainer.setQueues(currentQueue);
            currentContainer.start();
        }
    }

    @Bean
    public MessageConverter eventBusMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public ConnectionFactory eventBusConnectionFactory() {

        log.info("Creating query event bus connection on hostname: {" + this.hostname + "}");

        final CachingConnectionFactory connectionFactory = new CachingConnectionFactory(this.hostname, this.port);
        connectionFactory.setUsername(this.username);
        connectionFactory.setPassword(this.password);

        return connectionFactory;
    }

    @Bean
    @Autowired
    public RabbitAdmin eventBusRabbitAdmin(final ConnectionFactory eventBusConnectionFactory) {

        log.info("Creating query event bus RabbitMQ administrator template");
        return new RabbitAdmin(eventBusConnectionFactory);
    }

    @Bean
    @Autowired
    public RabbitTemplate eventBusRabbitTemplate(final ConnectionFactory eventBusConnectionFactory, final MessageConverter eventBusMessageConverter) {

        log.info("Creating query event bus RabbitMQ template with a custom JSON converter");
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(eventBusConnectionFactory);
        rabbitTemplate.setMessageConverter(eventBusMessageConverter);

        return rabbitTemplate;
    }

    @Bean
    public TopicExchange eventExchange() {

        log.info("Creating query event bus topic exchange with the name : {" + this.exchange + "}");
        return new TopicExchange(this.exchange, true, false);
    }
}
