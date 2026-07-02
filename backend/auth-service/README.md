# Auth Service

Authentication and Authorization Service for Smart Logistics Platform.

## Features
- JWT Authentication (Access/Refresh Tokens)
- Role Based Access Control (RBAC)
- Token Revocation (Redis Blacklist)
- Password Management
- Email OTP & Verification

## Tech Stack
- Spring Boot 3
- Java 21
- PostgreSQL
- Redis
- Testcontainers

## Build and Run
```bash
mvn clean install
mvn spring-boot:run
```

## Docker
```bash
docker build -t smartlogistics/auth-service .
docker run -p 8081:8081 smartlogistics/auth-service
```
