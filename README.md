# 🚌 Spring Command Either Bus

![License](https://img.shields.io/badge/License-MIT-blue.svg)
![Java Version](https://img.shields.io/badge/Java-25-blue)
![Test Coverage](https://img.shields.io/badge/coverage-90%25-brightgreen)

A lightweight command bus implementation for Spring Boot applications that enables centralized command handling using Spring's dependency injection capabilities and functional programming with Either types.

## 📚 Further Learning

This implementation is part of a comprehensive exploration of Hexagonal Architecture patterns. The concepts are covered in depth in:

**English Version**
*Decoupling by Design: A Pragmatic Approach to Hexagonal Architecture*

- [PDF](https://leanpub.com/decouplingbydesignapractitionersguidetohexagonalarchitecture)  
- [Kindle](https://a.co/d/4KwauyK)  
- [Paperback](https://a.co/d/cGQI8gX)  

**Versión en Español**  
*Desacoplamiento por Diseño: Una Guía Práctica para la Arquitectura Hexagonal*

- [PDF](https://leanpub.com/desacoplamientopordiseounaguaprcticaparalaarquitecturahexagonal)  
- [Kindle](https://amzn.eu/d/ic50CoH)  
- [Tapa blanda](https://amzn.eu/d/1fHOpN6)  

The book provides in-depth coverage of:

- Functional command handling with Either types
- Spring Boot integration for functional error handling
- Vavr Either pattern implementations
- Composable error handling strategies
- Command pattern variations with monadic results
- Hexagonal architecture with functional programming
- Real-world applications of Either in command buses
- Testing strategies for functional command handlers

## 🎯 Overview

This library provides a clean implementation of the Command pattern integrated with Spring's application context and functional error handling using Vavr's Either type. It automatically discovers command handlers and routes commands to their appropriate handlers, promoting loose coupling and separation of concerns in your application architecture.

## ✨ Features

- **🔍 Automatic Handler Discovery**: Automatically registers command handlers from Spring's application context
- **🎯 Type-Safe Command Routing**: Routes commands to their corresponding handlers based on generic type resolution
- **🔧 Spring Integration**: Leverages Spring's dependency injection for handler instantiation
- **⚡ Lightweight**: Minimal overhead with clean, focused API
- **🏗️ Hexagonal Architecture Support**: Perfect for implementing the command side of CQRS patterns
- **🛡️ Functional Error Handling**: Uses Vavr's Either type for robust error handling without exceptions
- **🔄 Either Monad Support**: Enables functional composition and error propagation

## 📦 Installation

Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.emedina.command</groupId>
    <artifactId>command-either-bus-spring-boot</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 🚀 Quick Start

### 1️⃣ Create a Command

```java
import com.emedina.sharedkernel.command.Command;

public class CreateUserCommand implements Command {
    private final String username;
    private final String email;
    
    public CreateUserCommand(String username, String email) {
        this.username = username;
        this.email = email;
    }
    
    // getters...
}
```

### 2️⃣ Create a Command Handler

```java
import com.emedina.sharedkernel.command.core.CommandHandler;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;

@Component
public class CreateUserCommandHandler implements CommandHandler<CreateUserCommand> {
    
    @Override
    public Either<String, Void> handle(CreateUserCommand command) {
        try {
            // Handle the command logic
            System.out.println("Creating user: " + command.getUsername());
            return Either.right(null);
        } catch (Exception e) {
            return Either.left("Failed to create user: " + e.getMessage());
        }
    }
}
```

### 3️⃣ Configure the Command Bus

```java
import com.emedina.command.spring.Registry;
import com.emedina.command.spring.SpringCommandBus;
import com.emedina.sharedkernel.command.core.CommandBus;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandBusConfiguration {
    
    @Bean
    public Registry registry(ApplicationContext applicationContext) {
        return new Registry(applicationContext);
    }
    
    @Bean
    public CommandBus commandBus(Registry registry) {
        return new SpringCommandBus(registry);
    }
}
```

### 4️⃣ Use the Command Bus

```java
import com.emedina.sharedkernel.command.core.CommandBus;
import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private CommandBus commandBus;
    
    public Either<String, Void> createUser(String username, String email) {
        CreateUserCommand command = new CreateUserCommand(username, email);
        return commandBus.execute(command);
    }
}
```

## 🏗️ Architecture

The command bus consists of three main components:

### 🚌 CommandBus

The main interface for executing commands. The `SpringCommandBus` implementation routes commands to their handlers and returns Either types for functional error handling.

### 📋 Registry

Maintains the mapping between command types and their handlers. It automatically discovers handlers from Spring's application context using generic type resolution.

### 🏭 CommandProvider

A factory that creates command handler instances using Spring's dependency injection capabilities.

## ⚙️ How It Works

1. **🔍 Handler Discovery**: On startup, the `Registry` scans the Spring application context for beans implementing `CommandHandler<T>`
2. **🧬 Type Resolution**: Uses Spring's `GenericTypeResolver` to determine which command type each handler processes
3. **📝 Handler Registration**: Maps command types to their corresponding handler providers
4. **🚀 Command Execution**: When a command is executed, the bus looks up the appropriate handler and delegates execution
5. **🛡️ Error Handling**: Returns Either<Error, Success> for functional error handling without exceptions

## 🔄 Either Type Benefits

The Either type provides several advantages:

- **🚫 No Exceptions**: Avoid exception-based error handling
- **🔗 Composable**: Chain operations functionally
- **🎯 Explicit**: Make error cases explicit in the type system
- **🛡️ Safe**: Compile-time safety for error handling

### Example with Error Handling

```java
public Either<UserError, Void> createUser(String username, String email) {
    return commandBus.execute(new CreateUserCommand(username, email))
        .mapLeft(error -> new UserError("User creation failed", error))
        .peek(success -> log.info("User created successfully"));
}
```

## 🧪 Testing

The library includes comprehensive unit and integration tests. Run tests with:

```bash
mvn test
```

### 📊 Test Coverage

- ✅ **Unit Tests**: All components tested with Mockito
- ✅ **Integration Tests**: Real Spring context validation
- ✅ **Edge Cases**: Missing handlers and empty contexts covered
- ✅ **90%+ Coverage**: Comprehensive test suite with JaCoCo

### 🔧 JaCoCo Coverage

Generate coverage reports:

```bash
mvn clean test jacoco:report
```

View the coverage report at `target/site/jacoco/index.html`

## 📋 Dependencies

| Dependency | Version | Purpose |
|------------|---------|---------|
| **Spring Framework** | 7.0.2 | Core Spring integration |
| **Java** | 25 | Runtime platform |
| **Vavr** | 0.11.0 | Functional programming with Either |
| **Shared Kernel Command Either Bus** | 1.0.0 | Command interfaces |

### Test Dependencies

| Dependency | Version | Purpose |
|------------|---------|---------|
| **JUnit Jupiter** | 6.0.2 | Testing framework |
| **Mockito** | 5.21.0 | Mocking framework |
| **AssertJ** | 3.27.6 | Fluent assertions |

## 🤝 Contributing

1. 🍴 Fork the repository
2. 🌿 Create a feature branch
3. ✅ Add tests for your changes
4. 🧪 Ensure all tests pass
5. 📊 Maintain 90%+ test coverage
6. 📤 Submit a pull request

## 📄 License

This project is part of the hexagonal architecture examples and follows the same licensing terms.

## 👨‍💻 Author

**Enrique Medina Montenegro**

---

## 🏷️ Tags

`spring-boot` `command-bus` `cqrs` `hexagonal-architecture` `ddd` `command-pattern` `spring-framework` `dependency-injection` `either` `functional-programming` `vavr` `error-handling`

---

*🎯 This library is designed to support clean architecture principles and CQRS patterns in Spring Boot applications with functional error handling using Either types.*
