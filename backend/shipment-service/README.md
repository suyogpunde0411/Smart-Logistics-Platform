# Shipment Service

The Shipment Service owns shipment creation, cargo metadata, pickup/drop scheduling, document/image metadata, pricing, status tracking, and matching-preparation events for the Smart Logistics Optimization Platform.

## Architecture

- Spring Boot 3 and Java 21 microservice registered with Eureka.
- Reads externalized configuration from Config Server when available.
- Uses PostgreSQL database `shipment_db`; no direct access to other service databases.
- Validates business owners through `user-service` using OpenFeign.
- Validates bearer tokens through the shared `auth-service` Feign contract when available, with gateway header fallback for existing deployments.
- Enforces owner-or-admin mutation rules in the service layer.
- Publishes lifecycle events to Kafka for downstream matching, tracking, notification, and analytics services.
- Uses the shared storage abstraction for document and image metadata URLs when clients do not provide an existing URL.
- Uses DTOs at API boundaries, MapStruct mapping, JPA repositories behind services, soft delete, audit fields, and optimistic locking through the shared base entity.

## Folder Structure

```text
backend/shipment-service/
|-- src/main/java/com/smartlogistics/shipmentservice/
|   |-- config/          # Security, OpenAPI, category bootstrap
|   |-- controller/      # Shipment, status, document, image, internal APIs
|   |-- dto/             # Request and response records
|   |-- entity/          # JPA shipment aggregate and related tables
|   |-- events/          # Kafka event definitions, producer, consumers
|   |-- exception/       # Domain exceptions and global handler
|   |-- mapper/          # MapStruct mapping
|   |-- repository/      # Spring Data repositories
|   `-- service/         # Business workflow logic
|-- src/main/resources/application.yml
|-- Dockerfile
`-- pom.xml
```

## Database Design

The service owns these tables in `shipment_db`:

- `shipments`: core shipment request, business owner, route, status, cargo type, weights, budget, expiry.
- `shipment_items`: cargo line items with quantity, weight, volume, declared value.
- `shipment_documents`: metadata for invoice, e-way bill, delivery challan, GST invoice, insurance copy, purchase order, and supporting documents.
- `shipment_images`: image metadata.
- `pickup_details` and `drop_details`: address, coordinates, contacts, scheduled/completed timestamps.
- `shipment_pricing`: base, distance, weight, insurance, tax, total, currency, pricing status.
- `shipment_status_history`: immutable status transition audit trail.
- `shipment_categories`: configurable cargo catalog.
- `shipment_dimensions`: length, width, height, calculated volume.
- `shipment_requirements`: special requirements attached to the shipment.

## API Summary

- `POST /api/v1/shipments` - create shipment.
- `GET /api/v1/shipments` - search shipments with business, status, category, weight, truck type, origin, destination, pickup date, delivery date, pagination, and sorting filters.
- `GET /api/v1/shipments/nearby` - radius search around pickup coordinates.
- `GET /api/v1/shipments/categories` - list active cargo categories.
- `GET /api/v1/shipments/{id}` - get shipment details.
- `PUT /api/v1/shipments/{id}` - update editable shipment details.
- `DELETE /api/v1/shipments/{id}` - soft delete editable shipment.
- `POST /api/v1/shipments/{id}/cancel` - cancel shipment.
- `POST /api/v1/shipments/{id}/items` and `GET /api/v1/shipments/{id}/items` - manage item metadata.
- `POST /api/v1/shipments/{id}/pickup` and `POST /api/v1/shipments/{id}/drop` - schedule pickup/drop.
- `GET|PUT /api/v1/shipments/{id}/pricing` - read or update pricing.
- `PUT /api/v1/shipments/{id}/status` and `GET /api/v1/shipments/{id}/status/history` - status workflow and audit.
- `POST|GET|DELETE /api/v1/shipments/{shipmentId}/documents` - document metadata.
- `POST|GET|DELETE /api/v1/shipments/{shipmentId}/images` - image metadata.
- `GET /api/v1/shipments/tracking/{trackingNumber}` - public shipment tracking.
- `GET /internal/shipments/{id}` - trusted inter-service shipment lookup.

OpenAPI is available at `http://localhost:8084/swagger-ui.html`.

## Kafka Events

Published topics:

- `shipment.created`
- `shipment.updated`
- `shipment.cancelled`
- `shipment.ready-for-matching`
- `shipment.delivered`
- `shipment.completed`
- `shipment.expired`

Consumed topics:

- `business.profile.updated`
- `truck.availability.changed`

When status changes to `AVAILABLE`, the service publishes `shipment.ready-for-matching`. Matching logic is intentionally owned by `matching-service`.
Overdue `CREATED` shipments are scanned by the shipment expiry scheduler and moved to `EXPIRED`, publishing `shipment.expired`.

## Feign Integrations

- `user-service`: validates that the business owner exists and is active before shipment creation.
- Auth is propagated by the API Gateway using `X-User-Id` and `X-User-Roles` headers consumed by local security filters.

## How To Run

From `backend`:

```bash
mvn -pl shipment-service -am spring-boot:run
```

Required local defaults:

- PostgreSQL database `shipment_db` on `localhost:5432`.
- Kafka on `localhost:9092`.
- Redis on `localhost:6379`.
- Eureka on `localhost:8761`.
- Config Server on `localhost:8888` is optional.

Override with environment variables:

```bash
SHIPMENT_DB_URL=jdbc:postgresql://localhost:5432/shipment_db
SHIPMENT_DB_USERNAME=postgres
SHIPMENT_DB_PASSWORD=postgres
KAFKA_BOOTSTRAP_SERVERS=localhost:9092
EUREKA_DEFAULT_ZONE=http://localhost:8761/eureka/
```

## Docker

Build the jar:

```bash
mvn -pl shipment-service -am clean package
```

Build and run the container:

```bash
docker build -t smart-logistics/shipment-service backend/shipment-service
docker run --rm -p 8084:8084 smart-logistics/shipment-service
```

## Testing

Run shipment tests with local dependencies:

```bash
mvn -pl shipment-service -am test
```

The current suite includes unit coverage for creation, category and pricing validation, owner/admin authorization, status transitions, expiry processing, and Kafka event trigger behavior. Add deeper controller/repository/Testcontainers coverage as the wider platform test harness stabilizes.
