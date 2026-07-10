# User Service

## Architecture
The `user-service` is a Spring Boot microservice responsible for managing all user information, profiles, addresses, emergency contacts, identity documents, and user preferences within the Smart Logistics Optimization Platform.

It is built adhering to a strictly isolated microservice architecture:
- **Database**: Dedicated PostgreSQL database (`user_db`).
- **Communication**: Interacts synchronously via OpenFeign (`auth-service`) and asynchronously via Apache Kafka.
- **Security**: Security and authentication tokens (JWT) are strictly managed externally by the `auth-service` and API Gateway. This service does not store passwords or generate tokens.

## Folder Structure
```
src/
├── main/
│   ├── java/com/smartlogistics/userservice/
│   │   ├── client/       # OpenFeign clients for inter-service calls
│   │   ├── controller/   # REST API Controllers (Swagger Documented)
│   │   ├── domain/
│   │   │   ├── entity/   # JPA Entities (UserProfile, DriverProfile, etc.)
│   │   │   └── repository/# Spring Data JPA Repositories
│   │   ├── dto/          # Data Transfer Objects & Validation
│   │   ├── exception/    # Global Exception Handling
│   │   ├── mapper/       # MapStruct Interfaces
│   │   ├── service/      # Business Logic & Kafka Event Producers
│   │   └── UserServiceApplication.java
│   └── resources/
│       └── application.yml # Configuration properties
```

## API Summary
The REST API exactly matches the finalized API Contract:
- `GET /api/v1/users` - Search & Paginate users (supports searching by Name, City, State, Business Name, Driver Name, Fleet Owner Name, Phone, Email, Status)
- `GET /api/v1/users/{id}` - Get user profile
- `PUT /api/v1/users/{id}` - Update user profile (validates Indian mobile format and enforces email/phone uniqueness)
- `GET|POST /api/v1/users/{id}/addresses` - Get/Add address
- `PUT|DELETE /api/v1/users/{id}/addresses/{addressId}` - Update/Soft-delete address
- `GET|POST /api/v1/users/{id}/emergency-contacts` - Get/Add emergency contact
- `PUT|DELETE /api/v1/users/{id}/emergency-contacts/{contactId}` - Update/Soft-delete emergency contact
- `PUT /api/v1/users/{id}/driver-profile` - Update Driver Profile (validates DL expiration/number, experience years)
- `PUT /api/v1/users/{id}/driver-profile/verify` - Verify Driver Profile (Internal/Admin API, publishes `DriverVerified` event)
- `PUT /api/v1/users/{id}/business-profile` - Update Business Profile (validates GST format)
- `PUT /api/v1/users/{id}/business-profile/verify` - Verify Business Profile (Internal/Admin API, publishes `BusinessVerified` event)
- `PUT /api/v1/users/{id}/fleet-owner-profile` - Update Fleet Owner Profile (validates company name and fleet size)
- `GET|PUT /api/v1/users/{id}/preferences` - Get/Update language and theme preferences (en, hi, mr)
- `GET|PUT /api/v1/users/{id}/settings` - Get/Update notification preferences (email, sms, push, 2FA)
- `GET|POST /api/v1/users/{id}/documents` - List/Upload identity document files (Aadhaar, PAN, GST, etc., metadata stored, S3 mock ready, publishes `DocumentUploaded` event)
- `DELETE /api/v1/users/{id}/documents/{documentId}` - Soft-delete document
- `GET|POST|DELETE /api/v1/users/{id}/profile-photo` - Get/Upload/Delete profile photo (supports JPEG, PNG, WEBP, <= 5MB)
- **User Synchronization**: Newly registered users are synced asynchronously by consuming events from the `"user-registration"` Kafka topic instead of REST APIs (creates profile + default preferences & settings, and publishes the `UserProfileCreated` event).

> **Note**: Complete interactive OpenAPI documentation is available at `/swagger-ui.html` when running the service.

## Audits, Tracing & Storage Abstractions
- **Structured Logging & Tracing**: A Servlet Filter captures incoming `X-Request-Id`, `X-Correlation-Id`, and `X-User-Id` headers. They are propagated via MDC to structured logs.
- **Audit Logs**: Base entity lifecycle callbacks (`@PrePersist` and `@PreUpdate`) automatically populate `createdBy` and `updatedBy` properties reading from the current thread-local `UserContext`.
- **Feign Propagation**: Outgoing microservice client calls propagate authentication and trace headers automatically.
- **Storage Layer**: An extensible `StorageService` abstraction is used for uploading files. The `MockStorageService` validates file types and sizes (max 5MB for photo formats JPEG/PNG/WEBP, max 10MB for documents) and simulates uploads returning mock URLs.

## Dependencies
- Java 21 & Spring Boot 3.3.x
- Spring Data JPA + PostgreSQL Driver
- Spring Web + Jakarta Validation
- Spring Cloud OpenFeign + Netflix Eureka Client
- Spring Kafka
- MapStruct & Lombok
- SpringDoc OpenAPI

## How to Run

1. Ensure Kafka, PostgreSQL, and Eureka Server (`service-registry`) are running.
2. Build the project:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## Testing Instructions
Run all Unit and Integration tests using Maven:
```bash
mvn clean test
```
