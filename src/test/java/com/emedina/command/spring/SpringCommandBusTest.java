package com.emedina.command.spring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.emedina.command.spring.fixtures.TestCommand;
import com.emedina.command.spring.fixtures.TestCommandHandler;

import io.vavr.control.Either;

/**
 * Unit tests for SpringCommandBus.
 *
 * @author Enrique Medina Montenegro
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SpringCommandBus")
class SpringCommandBusTest {

    @Mock
    private Registry registry;

    private SpringCommandBus commandBus;

    @BeforeEach
    void setUp() {
        commandBus = new SpringCommandBus(registry);
    }

    @Test
    @DisplayName("should execute command successfully when handler exists")
    void shouldExecuteCommandSuccessfully() {
        // given
        TestCommand command = new TestCommand("test message");
        TestCommandHandler handler = new TestCommandHandler();
        when(registry.<Throwable, TestCommand>get(TestCommand.class)).thenReturn(handler);

        // when
        Either<?, Void> result = commandBus.execute(command);

        // then
        verify(registry).get(TestCommand.class);
        assertThat(handler.wasExecuted()).isTrue();
        assertThat(handler.getLastCommand()).isEqualTo(command);
        assertThat(result.isRight()).isTrue();
    }

    @Test
    @DisplayName("should delegate to registry to get handler")
    void shouldDelegateToRegistryToGetHandler() {
        // given
        TestCommand command = new TestCommand("test message");
        TestCommandHandler handler = new TestCommandHandler();
        when(registry.<Throwable, TestCommand>get(TestCommand.class)).thenReturn(handler);

        // when
        commandBus.execute(command);

        // then
        verify(registry).get(TestCommand.class);
    }

    @Test
    @DisplayName("should throw exception when registry returns null handler")
    void shouldThrowExceptionWhenRegistryReturnsNullHandler() {
        // given
        TestCommand command = new TestCommand("test message");
        when(registry.get(TestCommand.class)).thenReturn(null);

        // when/then
        assertThatThrownBy(() -> commandBus.execute(command))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should throw exception when command is null")
    void shouldThrowExceptionWhenCommandIsNull() {
        // given
        TestCommand nullCommand = null;

        // when/then
        assertThatThrownBy(() -> commandBus.execute(nullCommand))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should return Either from handler")
    void shouldReturnEitherFromHandler() {
        // given
        TestCommand command = new TestCommand("test message");
        TestCommandHandler handler = new TestCommandHandler();
        when(registry.<Throwable, TestCommand>get(TestCommand.class)).thenReturn(handler);

        // when
        Either<?, Void> result = commandBus.execute(command);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isRight()).isTrue();
    }

}
