# Car Rental System

## Project Overview
A car rental administration system built with Spring Boot and Thymeleaf.

## Tech Stack
- Java 21 (Corretto)
- Spring Boot 4.0.5 (Web MVC, Data JPA, Security, Thymeleaf)
- PostgreSQL (main DB, runs in Docker)
- H2 (in-memory, for tests only)
- Lombok (use everywhere possible)
- Maven 3.9.14 (via wrapper)

## Build & Run

```bash
# Build
./mvnw clean package

# Run
./mvnw spring-boot:run

# Test
./mvnw test
```

## Architecture
Layered MVC architecture:
- **Controller** — handles HTTP requests, delegates to services
- **Service** — business logic
- **Repository** — data access via Spring Data JPA
- **DTOs** — used for data transfer between layers (never expose entities directly)

Base package: `org.ratifire.admin.carrentalsystem`

## Code Conventions
- **Language**: all code, comments, and commit messages in English
- **Lombok**: use wherever possible (`@Data`, `@Builder`, `@RequiredArgsConstructor`, `@Slf4j`, etc.)
- **Dependency injection**: constructor injection via `@RequiredArgsConstructor` (no `@Autowired`)
- **Entities**: use Lombok annotations, JPA annotations on fields
- **Services**: annotate with `@Service`, use `@Transactional` where appropriate
- **Controllers**: annotate with `@RestController` or `@Controller`

## Git Workflow
- Main branch: `master`
- New functionality: create a new branch, develop, then merge to `master`
- No branch naming convention enforced

## Commit Messages
Follow Conventional Commits:
```
feat: add vehicle listing endpoint
fix: correct date validation in booking service
refactor: extract rental price calculation
docs: update API documentation
test: add unit tests for UserService
```
