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

package io.acme.solution.application.conf;

import io.acme.solution.application.messaging.CommandHandler;
import io.acme.solution.command.Command;
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
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AssignableTypeFilter;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Spring boot configuration class for registering the queues on Rabbit for command bus
 */
@Configuration
public class CommandBusConfigurer {

    private static final Logger log = LoggerFactory.getLogger(CommandBusConfigurer.class);

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

    @Value("${command.package.base}")
    private String commandBasePackage;

    @Value("${command.handler.package.base}")
    private String handlerBasePackage;

    @Value("${command.bus.rabbit.queue.prefix}")
    private String queuePrefix;

    @Autowired
    private ApplicationContext context;

    @PostConstruct
    private void setup() {

        Queue currentQueue = null;
        String currentCommandType = null;
        SimpleMessageListenerContainer currentContainer = null;
        MessageListenerAdapter currentAdapter = null;

        final RabbitAdmin rabbitAdmin = this.context.getBean("commandBusRabbitAdmin", RabbitAdmin.class);
        final ConnectionFactory connectionFactory = this.context.getBean("commandBusConnectionFactory", ConnectionFactory.class);
        final TopicExchange exchange = this.context.getBean("commandExchange", TopicExchange.class);
        final MessageConverter converter = this.context.getBean("commandBusMessageConverter", MessageConverter.class);
        final Map<String, CommandHandler> commandHandlersRegistry = CommandHandlerUtils.buildCommandHandlersRegistry(this.handlerBasePackage, this.context);
        final ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(Command.class));

        for (BeanDefinition bean : scanner.findCandidateComponents(this.commandBasePackage)) {
            currentCommandType = bean.getBeanClassName().substring(bean.getBeanClassName().lastIndexOf('.') + 1);
            rabbitAdmin.declareQueue(currentQueue = new Queue(this.queuePrefix + currentCommandType));
            rabbitAdmin.declareBinding(BindingBuilder.bind(currentQueue).to(exchange).with(currentCommandType));

            if (commandHandlersRegistry.containsKey(bean.getBeanClassName())) {
                currentAdapter = new MessageListenerAdapter(commandHandlersRegistry.get(bean.getBeanClassName()), converter);

                currentContainer = new SimpleMessageListenerContainer(connectionFactory);
                currentContainer.setMessageListener(currentAdapter);
                currentContainer.setQueues(currentQueue);
                currentContainer.start();
            }
        }

    }

    @Bean
    public MessageConverter commandBusMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public ConnectionFactory commandBusConnectionFactory() {

        log.info("Creating command bus connection on hostname: {" + this.hostname + "}");

        final CachingConnectionFactory connectionFactory = new CachingConnectionFactory(this.hostname, this.port);
        connectionFactory.setUsername(this.username);
        connectionFactory.setPassword(this.password);

        return connectionFactory;
    }

    @Bean
    @Autowired
    public RabbitAdmin commandBusRabbitAdmin(final ConnectionFactory commandBusConnectionFactory) {

        log.info("Creating command bus RabbitMQ administrator template");
        return new RabbitAdmin(commandBusConnectionFactory);
    }

    @Bean
    @Autowired
    public RabbitTemplate commandBusRabbitTemplate(final ConnectionFactory commandBusConnectionFactory, final MessageConverter commandBusMessageConverter) {

        log.info("Creating command bus RabbitMQ template with a custom JSON converter");
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(commandBusConnectionFactory);
        rabbitTemplate.setMessageConverter(commandBusMessageConverter);

        return rabbitTemplate;
    }

    @Bean
    public TopicExchange commandExchange() {

        log.info("Creating command bus topic exchange with the name : {" + this.exchange + "}");
        return new TopicExchange(this.exchange, true, false);
    }
}
