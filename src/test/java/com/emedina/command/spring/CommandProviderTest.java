package com.emedina.command.spring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import com.emedina.command.spring.fixtures.TestCommandHandler;

/**
 * Unit tests for CommandProvider.
 * 
 * @author Enrique Medina Montenegro
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CommandProvider")
class CommandProviderTest {

    @Mock
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("should provide command handler from application context")
    void shouldProvideCommandHandlerFromApplicationContext() {
        // given
        TestCommandHandler expectedHandler = new TestCommandHandler();
        when(applicationContext.getBean(TestCommandHandler.class)).thenReturn(expectedHandler);

        CommandProvider<TestCommandHandler> provider = new CommandProvider<>(applicationContext,
            TestCommandHandler.class);

        // when
        TestCommandHandler actualHandler = provider.get();

        // then
        assertThat(actualHandler).isEqualTo(expectedHandler);
    }

    @Test
    @DisplayName("should delegate to application context for bean retrieval")
    void shouldDelegateToApplicationContextForBeanRetrieval() {
        // given
        TestCommandHandler handler = new TestCommandHandler();
        when(applicationContext.getBean(TestCommandHandler.class)).thenReturn(handler);

        CommandProvider<TestCommandHandler> provider = new CommandProvider<>(applicationContext,
            TestCommandHandler.class);

        // when
        provider.get();

        // then
        // Verify that the application context was called (implicitly tested by the mock)
        assertThat(provider.get()).isNotNull();
    }

    @Test
    @DisplayName("should return same instance on multiple calls if singleton")
    void shouldReturnSameInstanceOnMultipleCallsIfSingleton() {
        // given
        TestCommandHandler handler = new TestCommandHandler();
        when(applicationContext.getBean(TestCommandHandler.class)).thenReturn(handler);

        CommandProvider<TestCommandHandler> provider = new CommandProvider<>(applicationContext,
            TestCommandHandler.class);

        // when
        TestCommandHandler firstCall = provider.get();
        TestCommandHandler secondCall = provider.get();

        // then
        assertThat(firstCall).isEqualTo(secondCall);
    }
}
