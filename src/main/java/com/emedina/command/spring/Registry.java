package com.emedina.command.spring;

import com.emedina.sharedkernel.command.Command;
import com.emedina.sharedkernel.command.core.CommandHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * A registry that holds the mapping between a command and its handler using Spring's {@link ApplicationContext}.
 *
 * @author Enrique Medina Montenegro
 * @see CommandHandler
 */
public final class Registry {

    private final Map<Class<? extends Command>, CommandProvider<?>> providerMap = new HashMap<>();

    /**
     * Constructor-based dependency injection.
     *
     * @param applicationContext Spring's application context
     */
    public Registry(final ApplicationContext applicationContext) {
        String[] names = applicationContext.getBeanNamesForType(CommandHandler.class);
        for (String name : names) {
            this.register(applicationContext, name);
        }
    }

    /**
     * Looks up the name of the Bean (as a {@link CommandHandler}) in Spring's application context.
     *
     * @param applicationContext Spring's application context
     * @param name               of the bean as a command handler
     */
    @SuppressWarnings("unchecked")
    private void register(final ApplicationContext applicationContext, final String name) {
        Class<CommandHandler<?, ?>> handlerClass = (Class<CommandHandler<?, ?>>) applicationContext.getType(name);
        Class<?>[] generics = GenericTypeResolver.resolveTypeArguments(handlerClass, CommandHandler.class);

        if (generics == null || generics.length < 2) {
            throw new IllegalStateException("Could not resolve command type for handler: " + name);
        }

        Class<? extends Command> commandType = (Class<? extends Command>) generics[1];
        this.providerMap.put(commandType, new CommandProvider<>(applicationContext, handlerClass));
    }

    @SuppressWarnings("unchecked")
    <E, C extends Command> CommandHandler<E, C> get(final Class<C> commandClass) {
        CommandProvider<?> provider = this.providerMap.get(commandClass);
        return (CommandHandler<E, C>) provider.get();
    }

}
