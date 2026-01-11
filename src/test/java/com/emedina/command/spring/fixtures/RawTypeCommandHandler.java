package com.emedina.command.spring.fixtures;

import com.emedina.sharedkernel.command.Command;
import com.emedina.sharedkernel.command.core.CommandHandler;
import io.vavr.control.Either;

/**
 * Test fixture for a command handler without proper generic type information.
 * This is used to test error handling when generic types cannot be resolved.
 *
 * @author Enrique Medina Montenegro
 */
@SuppressWarnings("rawtypes")
public class RawTypeCommandHandler implements CommandHandler {

    @Override
    public Either<?, Void> handle(Command command) {
        return Either.right(null);
    }

}
