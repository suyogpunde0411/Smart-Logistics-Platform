# High-Level Architecture

The Smart Logistics Optimization Platform follows a robust Microservices Architecture, ensuring scalability, maintainability, and high availability.

## Architecture Diagram

```mermaid
graph TD
    %% Frontend Clients
    Client[React Frontend App]

    %% Gateway
    Gateway[API Gateway]

    %% Service Registry & Config
    Registry[Service Registry - Eureka]
    Config[Config Server]

    %% Microservices
    subgraph Microservices [Core Microservices]
        User[User Service]
        Truck[Truck Service]
        Shipment[Shipment Service]
        Matching[Matching Service]
        Tracking[Tracking Service]
        Notify[Notification Service]
        Review[Review Service]
        Admin[Admin Service]
        Analytics[Analytics Service]
        AI[Future AI Services]
    end

    %% Infrastructure Components
    Kafka[Apache Kafka Message Broker]
    Redis[Redis Cache]
    DB[(PostgreSQL Databases)]

    %% Connections
    Client --> Gateway
    Gateway --> User
    Gateway --> Truck
    Gateway --> Shipment
    Gateway --> Matching
    Gateway --> Tracking
    Gateway --> Notify
    Gateway --> Review
    Gateway --> Admin
    Gateway --> Analytics
    Gateway --> AI

    %% Registry & Config Connections
    Gateway -.-> Registry
    User -.-> Registry
    Truck -.-> Registry
    Shipment -.-> Registry
    Matching -.-> Registry

    User -.-> Config
    Truck -.-> Config
    Shipment -.-> Config

    %% DB & Cache Connections
    User --> DB
    Truck --> DB
    Shipment --> DB
    Tracking --> Redis
    Analytics --> DB

    %% Async Messaging
    User --> Kafka
    Shipment --> Kafka
    Matching --> Kafka
    Notify <-- Kafka
    Analytics <-- Kafka
```

## Architectural Explanation

1. **Frontend Clients**: The user-facing application is built in React (JavaScript). It handles UI, caching via React Query, and state management via Redux Toolkit.
2. **API Gateway**: Acts as the single entry point for all frontend requests, routing them to the appropriate backend microservices.
3. **Service Registry (Eureka)**: Maintains a dynamic registry of all available microservice instances, enabling the API Gateway and other services to discover each other dynamically.
4. **Config Server**: Externalizes all configuration properties (like DB credentials, Kafka endpoints) in a centralized, version-controlled repository.
5. **Microservices**: Each domain (Users, Trucks, Shipments) has its own independent microservice, scaling independently and owning its own data (database-per-service pattern).
6. **Kafka Message Broker**: Enables asynchronous, event-driven communication (e.g., triggering a Notification when a Shipment status changes).
7. **PostgreSQL**: The relational database used across transactional services. Each service typically manages its own isolated schema.
8. **Redis**: Used for high-speed caching and fast data retrieval, heavily utilized by the Tracking and Analytics services.
