package org.epo.cne.command.spring;

import io.vavr.control.Either;
import org.epo.cne.sharedkernel.command.Command;
import org.epo.cne.sharedkernel.command.core.CommandBus;
import org.epo.cne.sharedkernel.command.core.CommandHandler;
import org.springframework.context.annotation.Bean;

/**
 * Implementation of a command bus backed by Spring's registry.
 *
 * @author Enrique Medina Montenegro (em54029)
 */
public class SpringCommandBus implements CommandBus {

    private final Registry registry;

    /**
     * Creates a new instance with the given registry using constructor-based dependency injection.
     *
     * @param registry a wrapper around Spring's application context
     */
    public SpringCommandBus(final Registry registry) {
        this.registry = registry;
    }

    /**
     * Delegates the handling of the command to the corresponding {@link Bean} from Spring.
     *
     * @param command the command object
     * @return the result of the command's execution
     */
    @Override
    public <C extends Command> Either<?, Void> execute(final C command) {
        CommandHandler<C> commandHandler = (CommandHandler<C>) this.registry.get(command.getClass());
        return commandHandler.handle(command);
    }

}
