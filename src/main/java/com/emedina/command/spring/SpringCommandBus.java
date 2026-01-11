package com.emedina.command.spring;

import org.springframework.context.annotation.Bean;

import com.emedina.sharedkernel.command.Command;
import com.emedina.sharedkernel.command.core.CommandBus;
import com.emedina.sharedkernel.command.core.CommandHandler;

import io.vavr.control.Either;

/**
 * Implementation of a command bus backed by Spring's registry.
 *
 * @author Enrique Medina Montenegro
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
     * @param <C>     the type of the command
     * @return the result of the command's execution
     */
    @Override
    @SuppressWarnings("unchecked")
    public <C extends Command> Either<?, Void> execute(final C command) {
        Class<C> commandClass = (Class<C>) command.getClass();
        CommandHandler<C> commandHandler = this.registry.get(commandClass);
        return commandHandler.handle(command);
    }

}
