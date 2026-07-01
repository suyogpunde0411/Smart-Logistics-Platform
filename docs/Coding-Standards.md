# Coding Standards

This project enforces strict standards to ensure maintainability, testability, and future scalability.

## 1. Core Principles
- **SOLID**: All code must adhere to the 5 SOLID principles.
- **Clean Architecture**: Decouple domain logic from frameworks and APIs. 
- **DRY & KISS**: Do Not Repeat Yourself. Keep It Simple, Stupid.

## 2. Dependency Injection
- Use **Constructor Injection** universally.
- Field injection (`@Autowired` on fields) is strictly prohibited.

## 3. Data Flow and Access Boundaries
- **Controllers** must never access Repositories directly.
- **Repositories** are only to be accessed through Services.
- **Entities** should never cross API boundaries; always map to **DTOs**.
- Use **MapStruct** for all object mapping between Entities and DTOs.
- Circular dependencies between beans or services are prohibited.

## 4. Validation and Error Handling
- Use **Jakarta Validation** annotations (`@NotNull`, `@Size`, etc.) on DTOs.
- Implement a global exception handler (`@ControllerAdvice`) in every service to trap exceptions and return the standard `ApiResponse`.

## 5. Architectural Readiness
- Services should be designed with future Event-Driven Architecture (Kafka) in mind. Avoid tight synchronous coupling where asynchronous event publication is more appropriate.
