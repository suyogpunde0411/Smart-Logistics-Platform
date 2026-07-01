# Microservices Guidelines

## 1. Service List
- `api-gateway`: Edge service for routing, rate limiting, and global cross-cutting concerns.
- `service-registry`: Eureka server for dynamic service discovery.
- `config-server`: Centralized Spring Cloud Config.
- `user-service`: Manages User identities, authentication, roles, and profiles.
- `truck-service`: Manages Truck profiles, capacity, and current status.
- `shipment-service`: Handles the lifecycle of logistics shipments.
- `matching-service`: Connects shipments with available trucks.
- `tracking-service`: Real-time location and status tracking of shipments.
- `notification-service`: Sends Emails, SMS, and Push Notifications via event consumption.
- `review-service`: Handles ratings and reviews for completed shipments.
- `admin-service`: Administration tools and platform configuration.
- `analytics-service`: Data aggregation for dashboards and reports.
- `future-ai-service-placeholder`: Reserved for ML/AI modules.

## 2. Inter-Service Communication
- **Synchronous**: Use OpenFeign clients for required synchronous data fetching (e.g., getting user details from `user-service`).
- **Asynchronous**: Use Kafka for state changes and non-blocking tasks (e.g., triggering a notification after a shipment completes).

## 3. Common Library
All shared dependencies, DTOs (used across boundaries), Enums, standard exceptions, and base entities reside in `common-library` to ensure uniformity and reduce boilerplate code across the microservices.
