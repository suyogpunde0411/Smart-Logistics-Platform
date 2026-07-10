# Truck Service

The Truck Service manages fleet assets, locations, maintenance logs, insurance policies, documents, and coordinates geospatial available truck lookups for the Smart Logistics Optimization Platform.

---

## 1. Architecture & Design Principles
- **Clean Architecture & SOLID**: Strict layer boundaries. Entities encapsulate domain models; services hold logic and publishers; controllers expose clean endpoints.
- **Database Autonomy**: Owns `truck_db` completely. No shared queries. Inter-service constraints (e.g. owner verification) are resolved via asynchronous events and Feign clients.
- **Stateless Authentication**: Captures gateway-propagated roles via headers (`X-User-Id`, `X-User-Roles`) to implement local RBAC filters.

---

## 2. Directory Structure
```
backend/truck-service/
├── src/main/java/com/smartlogistics/truckservice/
│   ├── config/          # Spring Security, OpenAPI Swagger, servlet filters
│   ├── controller/      # API Controllers (External and VPC-Internal)
│   ├── dto/             # Immutable Java Record DTO payloads
│   ├── entity/          # JPA Domain Entities mapping tables
│   ├── events/          # Kafka Event Publishers & Definitions
│   ├── exception/       # Resource Handlers & Custom exception wrapper
│   ├── mapper/          # MapStruct Mapper interfaces
│   ├── repository/      # Data repositories with custom query parameters
│   └── service/         # Business coordination layer
└── src/test/java/       # Junit 5 Mockito Integration Tests
```

---

## 3. Database Schema (`truck_db`)
The schema coordinates a 1-to-1 capability and location tracking pattern:
- **`trucks`**: Core registry (`id`, `registration_number`, `owner_id`, `status`).
- **`truck_capacities`**: Weight (kg) and Volume (m³).
- **`truck_availabilities`**: Current state (`AVAILABLE`, `ON_TRIP`, `OUT_OF_SERVICE`) and active flag.
- **`truck_locations`**: Current GPS telemetry (`latitude`, `longitude`, `speed`, `heading`, `accuracy`).
- **`truck_location_history`**: Path logs auditing spatial transitions.
- **`truck_documents`**: Metadata for license certificate, permitting, or vehicle pollution verification.
- **`truck_insurances`**: Policy registry validation limits.

---

## 4. API Endpoints Summary

### External REST APIs
- `POST /api/v1/trucks` - Register a new truck (requires DRIVER, FLEET_OWNER, or ADMIN role)
- `GET /api/v1/trucks/{id}` - Fetch truck details
- `PUT /api/v1/trucks/{id}` - Update truck capacities
- `DELETE /api/v1/trucks/{id}` - Soft delete truck
- `GET /api/v1/trucks` - Search trucks with pagination, filtering, and sorting
- `PUT /api/v1/trucks/{id}/availability` - Toggle active status and availability state
- `PUT /api/v1/trucks/{id}/location` - Record current coordinates and log history
- `GET /api/v1/trucks/nearby` - Spatial radius search of available trucks (uses Haversine formula)
- `POST /api/v1/trucks/{id}/documents` - Upload vehicle document metadata
- `POST /api/v1/trucks/{id}/insurance` - Record insurance policy metadata
- `POST /api/v1/trucks/{id}/maintenance` - Log/schedule repairs or service details

### Internal REST APIs (Trusted calls inside subnets)
- `GET /internal/trucks/{id}` - Resolves inter-service details (used by Shipment / Matching services)

---

## 5. Event-Driven Messaging (Kafka)
The service publishes transactions on:
- `truck.registered`: Fired when a truck is created.
- `truck.updated`: Fired when capacities change.
- `truck.deleted`: Fired when a vehicle is decommissioned.
- `truck.availability.changed`: Fired when availability state toggles.
- `truck.location.updated`: Fired when GPS tracking updates coordinates.
- `truck.maintenance.scheduled`: Fired when repairs log are scheduled.
- `truck.insurance.expired`: Fired when an expired insurance policy metadata is added.

---

## 6. How to Run & Verify

### Build and Package
Run inside `/backend/truck-service`:
```bash
mvn clean install
```

### Run Tests
Execute the Junit 5 test suite (Controller, Service, Repository, and Haversine spatial queries tests):
```bash
mvn test
```

### OpenAPI Documentation
Available locally at: `http://localhost:8083/swagger-ui.html`
