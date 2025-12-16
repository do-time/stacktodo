# StackTodo Backend

## Project Overview
StackTodo is a comprehensive task management application. This repository contains the backend services, built with Spring Boot, responsible for managing tasks, categories, members, and other related entities. It provides a robust API for a potential frontend application to consume.

## Technologies Used
*   **Java 17**
*   **Spring Boot 3.x**: Microservice framework
*   **Gradle**: Build automation tool
*   **JPA/Hibernate**: For data persistence
*   **Spring Security & JWT**: For authentication and authorization
*   **OAuth2**: For external authentication
*   **Kafka**: For asynchronous messaging (notifications)
*   **Jasypt**: For encrypting configuration properties

## Project Structure
The project follows a modular and clean architecture pattern, organizing code by domain features (e.g., `category`, `member`, `task`).

```
stacktodo-be/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── io/app/stacktodobe/
│   │   │       ├── StacktodoBeApplication.java  // Main Spring Boot application
│   │   │       ├── category/                 // Category domain module
│   │   │       ├── common/                   // Common utilities and base classes
│   │   │       ├── hashtag/                  // Hashtag domain module
│   │   │       ├── infrastructure/           // Infrastructure concerns (security, config)
│   │   │       ├── member/                   // Member domain module
│   │   │       ├── notification/             // Notification domain module
│   │   │       ├── task/                     // Task domain module
│   │   │       ├── tasklist/                 // Task List domain module
│   │   │       └── workspace/                // Workspace domain module
│   │   └── resources/
│   │       └── application.yml               // Spring Boot configuration
│   └── test/
│       ├── java/
│       │   └── io/app/stacktodobe/           // Unit and Integration tests
│       └── resources/
│           └── application-test.yml          // Test-specific configuration
├── build.gradle                              // Gradle build script
├── docker-compose.yml                        // Docker Compose configuration
└── gradlew                                   // Gradle Wrapper script
```

Each domain module typically includes:
*   `adapter/`: Adapters for inbound (web) and outbound (persistence) communication.
*   `application/`: Application services, commands, and ports.
*   `domain/`: Core business logic, models, and exceptions.
*   `exception/`: Domain-specific exceptions and handlers.
*   `mapper/`: DTO/Entity mapping.

## Getting Started

### Prerequisites
*   Java Development Kit (JDK) 17 or higher
*   Docker (for running Kafka and Database)

### Building the Project
To build the project, navigate to the `stacktodo-be` directory and run:
```bash
./gradlew clean build
```

### Running with Docker Compose
To run the application along with its dependencies (e.g., database, Kafka) using Docker Compose:
```bash
docker-compose up --build
```

### Running Locally
You can run the application directly using Gradle:
```bash
./gradlew bootRun
```

## API Endpoints
(Further documentation on API endpoints will be provided here, e.g., using Swagger/OpenAPI.)

## Contributing
(Guidelines for contributing to the project will be added here.)

## License
This project is licensed under the [LICENSE](LICENSE) file.
