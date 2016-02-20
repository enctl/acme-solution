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

package io.acme.solution.api.conf;

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

        log.info("Creating rabbit connection for command bus, publishing, on host: {" + this.hostname + "}");

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

        log.info("Plugging in custom json serializer for RabbitMQ");

        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);

        return rabbitTemplate;
    }

    @Bean
    public TopicExchange commandExchange() {
        log.info("Creating a durable topic exchange for the command bus with name: {" + this.exchange +
                "} and auto-delete is: {" + this.deletable + "}");

        return new TopicExchange(this.exchange, true, this.deletable);
    }
}
