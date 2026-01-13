package com.emedina.command.spring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import com.emedina.command.spring.fixtures.AnotherTestCommand;
import com.emedina.command.spring.fixtures.AnotherTestCommandHandler;
import com.emedina.command.spring.fixtures.RawTypeCommandHandler;
import com.emedina.command.spring.fixtures.TestCommand;
import com.emedina.command.spring.fixtures.TestCommandHandler;
import com.emedina.sharedkernel.command.core.CommandHandler;

/**
 * Unit tests for Registry.
 *
 * @author Enrique Medina Montenegro
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Registry")
class RegistryTest {

    @Mock
    private ApplicationContext applicationContext;

    private Registry registry;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void setupWithHandlers() {
        when(applicationContext.getBeanNamesForType(CommandHandler.class))
            .thenReturn(new String[] { "testCommandHandler", "anotherTestCommandHandler" });

        when(applicationContext.getType("testCommandHandler"))
            .thenReturn((Class) TestCommandHandler.class);
        when(applicationContext.getType("anotherTestCommandHandler"))
            .thenReturn((Class) AnotherTestCommandHandler.class);

        when(applicationContext.getBean(TestCommandHandler.class))
            .thenReturn(new TestCommandHandler());
        when(applicationContext.getBean(AnotherTestCommandHandler.class))
            .thenReturn(new AnotherTestCommandHandler());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void setupWithTestHandlerOnly() {
        when(applicationContext.getBeanNamesForType(CommandHandler.class))
            .thenReturn(new String[] { "testCommandHandler" });

        when(applicationContext.getType("testCommandHandler"))
            .thenReturn((Class) TestCommandHandler.class);

        when(applicationContext.getBean(TestCommandHandler.class))
            .thenReturn(new TestCommandHandler());
    }

    private void setupWithoutHandlers() {
        when(applicationContext.getBeanNamesForType(CommandHandler.class))
            .thenReturn(new String[] {});
    }

    @Test
    @DisplayName("should register command handlers during construction")
    void shouldRegisterCommandHandlersDuringConstruction() {
        // given
        setupWithHandlers();

        // when
        registry = new Registry(applicationContext);

        // then
        CommandHandler<Throwable, TestCommand> testHandler = registry.get(TestCommand.class);
        CommandHandler<Throwable, AnotherTestCommand> anotherHandler = registry.get(AnotherTestCommand.class);

        assertThat(testHandler).isNotNull();
        assertThat(testHandler).isInstanceOf(TestCommandHandler.class);
        assertThat(anotherHandler).isNotNull();
        assertThat(anotherHandler).isInstanceOf(AnotherTestCommandHandler.class);
    }

    @Test
    @DisplayName("should return correct handler for command type")
    void shouldReturnCorrectHandlerForCommandType() {
        // given
        setupWithTestHandlerOnly();
        registry = new Registry(applicationContext);

        // when
        CommandHandler<Throwable, TestCommand> handler = registry.get(TestCommand.class);

        // then
        assertThat(handler).isInstanceOf(TestCommandHandler.class);

        TestCommand command = new TestCommand("test");
        handler.handle(command);

        TestCommandHandler testHandler = (TestCommandHandler) handler;
        assertThat(testHandler.wasExecuted()).isTrue();
        assertThat(testHandler.getLastCommand()).isEqualTo(command);
    }

    @Test
    @DisplayName("should handle multiple command types")
    void shouldHandleMultipleCommandTypes() {
        // given
        setupWithHandlers();
        registry = new Registry(applicationContext);

        // when
        CommandHandler<Throwable, TestCommand> testHandler = registry.get(TestCommand.class);
        CommandHandler<Throwable, AnotherTestCommand> anotherHandler = registry.get(AnotherTestCommand.class);

        // then
        assertThat(testHandler).isInstanceOf(TestCommandHandler.class);
        assertThat(anotherHandler).isInstanceOf(AnotherTestCommandHandler.class);
        assertThat(testHandler).isNotSameAs(anotherHandler);
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when no handler registered for command type")
    void shouldThrowIllegalArgumentExceptionWhenNoHandlerRegisteredForCommandType() {
        // given
        setupWithoutHandlers();
        registry = new Registry(applicationContext);

        // when & then
        assertThatThrownBy(() -> registry.get(TestCommand.class))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("No command handler registered for:");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException for any command type when application context is empty")
    void shouldThrowIllegalArgumentExceptionWhenApplicationContextIsEmpty() {
        // given
        setupWithoutHandlers();

        // when
        registry = new Registry(applicationContext);

        // then
        assertThatThrownBy(() -> registry.get(TestCommand.class))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("No command handler registered for:");
        assertThatThrownBy(() -> registry.get(AnotherTestCommand.class))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("No command handler registered for:");
    }

    @Test
    @DisplayName("should throw IllegalStateException when handler has no generic type information")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    void shouldThrowIllegalStateExceptionWhenHandlerHasNoGenericTypeInformation() {
        // given
        when(applicationContext.getBeanNamesForType(CommandHandler.class))
            .thenReturn(new String[] { "rawTypeCommandHandler" });
        when(applicationContext.getType("rawTypeCommandHandler"))
            .thenReturn((Class) RawTypeCommandHandler.class);

        // when & then
        assertThatThrownBy(() -> new Registry(applicationContext))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Could not resolve command type for handler: rawTypeCommandHandler");
    }

    @Test
    @DisplayName("should throw IllegalStateException when generic type resolution returns null")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    void shouldThrowIllegalStateExceptionWhenGenericTypeResolutionReturnsNull() {
        // given
        when(applicationContext.getBeanNamesForType(CommandHandler.class))
            .thenReturn(new String[] { "rawTypeCommandHandler" });
        when(applicationContext.getType("rawTypeCommandHandler"))
            .thenReturn((Class) RawTypeCommandHandler.class);

        // when & then
        assertThatThrownBy(() -> new Registry(applicationContext))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Could not resolve command type for handler: rawTypeCommandHandler");
    }

}
