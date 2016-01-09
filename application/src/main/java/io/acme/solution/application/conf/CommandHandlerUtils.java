package io.acme.solution.application.conf;

import io.acme.solution.application.messaging.CommandHandler;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.ClassUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilities for supporting the command handlers
 */
public class CommandHandlerUtils {

    private CommandHandlerUtils() {

    }

    public static Map<String, CommandHandler> buildCommandHandlersRegistry(final String basePackage,
                                                                           final ApplicationContext context) {

        final Map<String, CommandHandler> registry = new HashMap<>();
        final ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        final AutowireCapableBeanFactory beanFactory = context.getAutowireCapableBeanFactory();
        scanner.addIncludeFilter(new AssignableTypeFilter(CommandHandler.class));

        CommandHandler currentHandler = null;

        for (BeanDefinition bean : scanner.findCandidateComponents(basePackage)) {
            currentHandler = (CommandHandler) beanFactory.createBean(ClassUtils.resolveClassName(bean.getBeanClassName(), context.getClassLoader()),
                    AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
            registry.put(currentHandler.getInterest().getName(), currentHandler);
        }

        return registry;
    }
}
