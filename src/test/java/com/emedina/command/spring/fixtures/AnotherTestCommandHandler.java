package com.emedina.command.spring.fixtures;

import com.emedina.sharedkernel.command.core.CommandHandler;
import io.vavr.control.Either;

/**
 * Another test command handler fixture for testing multiple command types.
 *
 * @author Enrique Medina Montenegro
 */
public class AnotherTestCommandHandler implements CommandHandler<Throwable, AnotherTestCommand> {

    private boolean wasExecuted = false;
    private AnotherTestCommand lastCommand;

    @Override
    public Either<Throwable, Void> handle(AnotherTestCommand command) {
        this.wasExecuted = true;
        this.lastCommand = command;
        return Either.right(null);
    }

    public boolean wasExecuted() {
        return wasExecuted;
    }

    public AnotherTestCommand getLastCommand() {
        return lastCommand;
    }

    public void reset() {
        this.wasExecuted = false;
        this.lastCommand = null;
    }
}
