package com.emedina.command.spring.fixtures;

import com.emedina.sharedkernel.command.core.CommandHandler;

import io.vavr.control.Either;

/**
 * Test command handler fixture for testing the command bus.
 * 
 * @author Enrique Medina Montenegro
 */
public class TestCommandHandler implements CommandHandler<TestCommand> {

    private boolean wasExecuted = false;
    private TestCommand lastCommand;

    @Override
    public Either<?, Void> handle(TestCommand command) {
        this.wasExecuted = true;
        this.lastCommand = command;
        return Either.right(null);
    }

    public boolean wasExecuted() {
        return wasExecuted;
    }

    public TestCommand getLastCommand() {
        return lastCommand;
    }

    public void reset() {
        this.wasExecuted = false;
        this.lastCommand = null;
    }
}
