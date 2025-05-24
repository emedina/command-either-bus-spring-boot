package com.emedina.command.spring;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.emedina.command.spring.fixtures.AnotherTestCommand;
import com.emedina.command.spring.fixtures.AnotherTestCommandHandler;
import com.emedina.command.spring.fixtures.TestCommand;
import com.emedina.command.spring.fixtures.TestCommandHandler;
import com.emedina.sharedkernel.command.core.CommandBus;

import io.vavr.control.Either;

/**
 * Integration tests for SpringCommandBus with real Spring context.
 * 
 * @author Enrique Medina Montenegro
 */
@DisplayName("SpringCommandBus Integration")
class SpringCommandBusIntegrationTest {

    @Configuration
    static class TestConfiguration {

        @Bean
        public TestCommandHandler testCommandHandler() {
            return new TestCommandHandler();
        }

        @Bean
        public AnotherTestCommandHandler anotherTestCommandHandler() {
            return new AnotherTestCommandHandler();
        }

        @Bean
        public Registry registry(AnnotationConfigApplicationContext applicationContext) {
            return new Registry(applicationContext);
        }

        @Bean
        public CommandBus commandBus(Registry registry) {
            return new SpringCommandBus(registry);
        }
    }

    @Test
    @DisplayName("should execute commands with real Spring context")
    void shouldExecuteCommandsWithRealSpringContext() {
        // given
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            TestConfiguration.class)) {
            CommandBus commandBus = context.getBean(CommandBus.class);
            TestCommand testCommand = new TestCommand("integration test");
            AnotherTestCommand anotherCommand = new AnotherTestCommand(42);

            // when
            Either<?, Void> testResult = commandBus.execute(testCommand);
            Either<?, Void> anotherResult = commandBus.execute(anotherCommand);

            // then
            assertThat(testResult.isRight()).isTrue();
            assertThat(anotherResult.isRight()).isTrue();

            // Verify handlers were executed
            TestCommandHandler testHandler = context.getBean(TestCommandHandler.class);
            AnotherTestCommandHandler anotherHandler = context.getBean(AnotherTestCommandHandler.class);

            assertThat(testHandler.wasExecuted()).isTrue();
            assertThat(testHandler.getLastCommand()).isEqualTo(testCommand);
            assertThat(anotherHandler.wasExecuted()).isTrue();
            assertThat(anotherHandler.getLastCommand()).isEqualTo(anotherCommand);
        }
    }

    @Test
    @DisplayName("should handle multiple commands of same type")
    void shouldHandleMultipleCommandsOfSameType() {
        // given
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            TestConfiguration.class)) {
            CommandBus commandBus = context.getBean(CommandBus.class);
            TestCommand firstCommand = new TestCommand("first command");
            TestCommand secondCommand = new TestCommand("second command");

            // when
            Either<?, Void> firstResult = commandBus.execute(firstCommand);
            Either<?, Void> secondResult = commandBus.execute(secondCommand);

            // then
            assertThat(firstResult.isRight()).isTrue();
            assertThat(secondResult.isRight()).isTrue();

            // Verify the last command was the second one
            TestCommandHandler handler = context.getBean(TestCommandHandler.class);
            assertThat(handler.getLastCommand()).isEqualTo(secondCommand);
        }
    }

    @Test
    @DisplayName("should work with singleton handlers")
    void shouldWorkWithSingletonHandlers() {
        // given
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            TestConfiguration.class)) {
            CommandBus commandBus = context.getBean(CommandBus.class);
            TestCommand command = new TestCommand("singleton test");

            // when
            commandBus.execute(command);
            commandBus.execute(command);

            // then
            TestCommandHandler handler1 = context.getBean(TestCommandHandler.class);
            TestCommandHandler handler2 = context.getBean(TestCommandHandler.class);

            // Spring beans are singletons by default
            assertThat(handler1).isSameAs(handler2);
            assertThat(handler1.wasExecuted()).isTrue();
        }
    }
}
