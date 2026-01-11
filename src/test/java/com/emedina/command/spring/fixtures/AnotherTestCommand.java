package com.emedina.command.spring.fixtures;

import com.emedina.sharedkernel.command.Command;

/**
 * Another test command fixture for testing multiple command types.
 *
 * @author Enrique Medina Montenegro
 */
public class AnotherTestCommand implements Command {

    private final int value;

    public AnotherTestCommand(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        AnotherTestCommand that = (AnotherTestCommand) obj;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public String toString() {
        return "AnotherTestCommand{value=" + value + "}";
    }
}
