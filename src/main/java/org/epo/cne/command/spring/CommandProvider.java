package org.epo.cne.command.spring;

import org.epo.cne.sharedkernel.command.core.CommandHandler;
import org.springframework.context.ApplicationContext;

/**
 * Creates a command handler that makes use of Spring's dependency injection capabilities.
 *
 * @param <H> type of the command handler
 * @author Enrique Medina Montenegro (em54029)
 */
@SuppressWarnings("unchecked")
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
