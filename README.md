# Car Rental System

A car reservation system built with Spring Boot and Thymeleaf. Users can browse available cars (Sedan, SUV, Van), make reservations for a desired date/time and number of days, and manage their bookings.

## Tech Stack

- Java 21, Spring Boot 4.0.5
- Spring Security (Basic Auth + Form Login)
- Spring Data JPA, PostgreSQL, H2 (local/test)
- Thymeleaf + JavaScript (SPA-style)
- Maven, Lombok

## Run

### Local (H2, no Docker)
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

### With PostgreSQL (Docker)
```bash
docker run -d --name car-rental-postgres \
  -e POSTGRES_DB=car_rental -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin \
  -p 5432:5432 postgres:17

./mvnw spring-boot:run
```

### Tests
```bash
./mvnw test
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/cars` | List cars (optional `?type=SEDAN`) |
| GET | `/api/cars/{id}` | Get car by id |
| GET | `/api/reservations?userId=X` | User's reservations |
| POST | `/api/reservations` | Create reservation |
| DELETE | `/api/reservations/{id}` | Delete reservation |
| GET | `/api/users/me` | Current user info |

All API endpoints require authentication (HTTP Basic or session).

## Default Users (local profile)

| Email | Password |
|-------|----------|
| john@example.com | password |
| jane@example.com | password |
| bob@example.com | password |
