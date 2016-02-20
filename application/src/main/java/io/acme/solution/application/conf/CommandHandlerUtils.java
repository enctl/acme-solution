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
