# Bot Guard

Bot Guard is a security project designed to evaluate and mitigate risks associated with user registrations and logins.
It includes features such as bot detection, impossible travel detection, and multiple aliases per email detection to enhance the security of your application.

## Features

- Bot check
- Login risks evaluation
    - Impossible travel detection
- Registration risks evaluation
    - Temporary email detection
    - Multiple aliases per email detection

## Documentation

- [Documentation](docs/design.md)
- [Swagger](http://localhost:8080/bot-guard/swagger-ui.html)
- [Open API](http://localhost:8080/bot-guard/v3/api-docs)

## Run Locally

Clone the project

```bash
   git clone https://github.com/madaaraisok/bot-guard.git
```

Go to the project directory

```bash
   cd bot-guard
```

Install dependencies

```bash
   ./mvnw install
```

Start the server

```bash
   ./mvnw spring-boot:run
```

## Running e2e tests

To run e2e tests, run the following command

```bash
   ./mvnw verify -pl e2e-tests -P e2e-tests,e2e-tests-dev -am
```

## Code Coverage

The code coverage report can be found [here](coverage/target/site/jacoco-aggregate/index.html)

## Modules

There are several modules in Bot Guard. Here is a quick overview:

- app: The main module that contains the application logic.
- coverage: The module that contains the coverage report.
- e2e-tests: The module that contains the end-to-end tests.
- docs: The module that contains the documentation.

## Package structure

This project tries to follow Hexagonal Architecture, which includes the following concepts:

### Source code package structure

Here is a quick overview of the source code package structure, following Hexagonal Architecture principles:

- application: Contains application services, which orchestrate the use cases of the application. These services are the entry points for the application's core logic and interact with the domain layer.
- domain: Contains domain classes, which represent the core business logic and rules. This layer is independent of any external systems and frameworks, ensuring that the business logic remains pure and testable.
- infrastructure: Contains infrastructure classes, which handle the implementation details of external systems and frameworks, such as databases, messaging systems, and web services. This layer provides the necessary adapters to interact with the outside world.

### Test code package structure

Here is a quick overview of the test code package structure, aligned with Hexagonal Architecture principles:

- common: Contains common test classes, which provide shared utilities and configurations for the tests.
- functional: Contains functional tests, which verify the application's behavior from an end-to-end perspective, ensuring that all components work together as expected.
- integration: Contains integration tests, which test the interaction between the application and external systems, such as databases and web services.
- resiliency: Contains resiliency tests, which assess the application's ability to handle failures and recover gracefully.
- unit: Contains unit tests, which test individual components in isolation, ensuring that each piece of the application works correctly on its own.

## Tech Stack

Java 21, Spring Boot