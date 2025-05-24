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

    @Test
    @DisplayName("should register and retrieve command handlers correctly")
    void shouldRegisterAndRetrieveCommandHandlersCorrectly() {
        // given
        String[] handlerNames = { "testCommandHandler", "anotherTestCommandHandler" };
        when(applicationContext.getBeanNamesForType(CommandHandler.class)).thenReturn(handlerNames);
        when(applicationContext.getType("testCommandHandler")).thenReturn((Class) TestCommandHandler.class);
        when(applicationContext.getType("anotherTestCommandHandler")).thenReturn(
            (Class) AnotherTestCommandHandler.class);
        when(applicationContext.getBean(TestCommandHandler.class)).thenReturn(new TestCommandHandler());
        when(applicationContext.getBean(AnotherTestCommandHandler.class)).thenReturn(new AnotherTestCommandHandler());

        // when
        Registry registry = new Registry(applicationContext);

        // then
        CommandHandler<TestCommand> testHandler = registry.get(TestCommand.class);
        CommandHandler<AnotherTestCommand> anotherHandler = registry.get(AnotherTestCommand.class);

        assertThat(testHandler).isInstanceOf(TestCommandHandler.class);
        assertThat(anotherHandler).isInstanceOf(AnotherTestCommandHandler.class);
    }

    @Test
    @DisplayName("should handle empty application context")
    void shouldHandleEmptyApplicationContext() {
        // given
        String[] emptyHandlerNames = {};
        when(applicationContext.getBeanNamesForType(CommandHandler.class)).thenReturn(emptyHandlerNames);

        // when
        Registry registry = new Registry(applicationContext);

        // then
        assertThatThrownBy(() -> registry.get(TestCommand.class))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should register single command handler")
    void shouldRegisterSingleCommandHandler() {
        // given
        String[] handlerNames = { "testCommandHandler" };
        when(applicationContext.getBeanNamesForType(CommandHandler.class)).thenReturn(handlerNames);
        when(applicationContext.getType("testCommandHandler")).thenReturn((Class) TestCommandHandler.class);
        when(applicationContext.getBean(TestCommandHandler.class)).thenReturn(new TestCommandHandler());

        // when
        Registry registry = new Registry(applicationContext);

        // then
        CommandHandler<TestCommand> handler = registry.get(TestCommand.class);
        assertThat(handler).isInstanceOf(TestCommandHandler.class);
    }

    @Test
    @DisplayName("should throw exception when handler not found")
    void shouldThrowExceptionWhenHandlerNotFound() {
        // given
        String[] emptyHandlerNames = {};
        when(applicationContext.getBeanNamesForType(CommandHandler.class)).thenReturn(emptyHandlerNames);

        // when
        Registry registry = new Registry(applicationContext);

        // then
        assertThatThrownBy(() -> registry.get(TestCommand.class))
            .isInstanceOf(NullPointerException.class);
    }
}
