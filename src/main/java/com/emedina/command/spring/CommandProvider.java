package com.emedina.command.spring;

import org.springframework.context.ApplicationContext;

import com.emedina.sharedkernel.command.core.CommandHandler;

/**
 * Creates a command handler that makes use of Spring's dependency injection capabilities.
 *
 * @param <H> type of the command handler
 * @author Enrique Medina Montenegro
 */
class CommandProvider<H extends CommandHandler<?>> {

    private final ApplicationContext applicationContext;
    private final Class<H> type;

    /**
     * Constructor-based dependency injection.
     *
     * @param applicationContext Spring's application context
     * @param type               of the command handler
     */
    CommandProvider(final ApplicationContext applicationContext, final Class<H> type) {
        this.applicationContext = applicationContext;
        this.type = type;
    }

    public H get() {
        return this.applicationContext.getBean(this.type);
    }

}
