package com.emedina.command.spring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
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

    private CommandProvider<TestCommandHandler> commandProvider;

    @BeforeEach
    void setUp() {
        commandProvider = new CommandProvider<>(applicationContext, TestCommandHandler.class);
    }

    @Test
    @DisplayName("should get bean from application context")
    void shouldGetBeanFromApplicationContext() {
        // given
        TestCommandHandler expectedHandler = new TestCommandHandler();
        when(applicationContext.getBean(TestCommandHandler.class)).thenReturn(expectedHandler);

        // when
        TestCommandHandler actualHandler = commandProvider.get();

        // then
        verify(applicationContext).getBean(TestCommandHandler.class);
        assertThat(actualHandler).isSameAs(expectedHandler);
    }

    @Test
    @DisplayName("should delegate to application context getBean method")
    void shouldDelegateToApplicationContextGetBeanMethod() {
        // given
        TestCommandHandler handler = new TestCommandHandler();
        when(applicationContext.getBean(TestCommandHandler.class)).thenReturn(handler);

        // when
        commandProvider.get();

        // then
        verify(applicationContext).getBean(TestCommandHandler.class);
    }

    @Test
    @DisplayName("should return same instance as application context")
    void shouldReturnSameInstanceAsApplicationContext() {
        // given
        TestCommandHandler handler = new TestCommandHandler();
        when(applicationContext.getBean(TestCommandHandler.class)).thenReturn(handler);

        // when
        TestCommandHandler result1 = commandProvider.get();
        TestCommandHandler result2 = commandProvider.get();

        // then
        assertThat(result1).isSameAs(handler);
        assertThat(result2).isSameAs(handler);
        assertThat(result1).isSameAs(result2);
    }

    @Test
    @DisplayName("should handle null return from application context")
    void shouldHandleNullReturnFromApplicationContext() {
        // given
        when(applicationContext.getBean(TestCommandHandler.class)).thenReturn(null);

        // when
        TestCommandHandler result = commandProvider.get();

        // then
        assertThat(result).isNull();
    }

}
