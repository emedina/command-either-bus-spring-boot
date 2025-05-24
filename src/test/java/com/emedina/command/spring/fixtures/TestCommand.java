package com.emedina.command.spring.fixtures;

import com.emedina.sharedkernel.command.Command;

/**
 * Test command fixture for testing the command bus.
 * 
 * @author Enrique Medina Montenegro
 */
public class TestCommand implements Command {

    private final String message;

    public TestCommand(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        TestCommand that = (TestCommand) obj;
        return message != null ? message.equals(that.message) : that.message == null;
    }

    @Override
    public int hashCode() {
        return message != null ? message.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TestCommand{message='" + message + "'}";
    }
}
