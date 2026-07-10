# Shared Logistics Library

This is a reusable Maven module for the Smart Logistics Optimization Platform. It provides domain objects, validators, utilities, exceptions, security helpers, and auditing models that all logistics microservices (Shipment, Matching, Tracking, Analytics) reuse.

---

## 1. Core Modules

### 1.1 Enums & Measurement Units (`com.smartlogistics.shared.enums`)
- **Core Domain Enums**: `VehicleType`, `TruckType`, `CargoType`, `ShipmentStatus`, `TripStatus`, `BidStatus`, `MatchStatus`, and `DocumentType`.
- **Measurement Units**: `WeightUnit` (KG, TON), `VolumeUnit` (CUBIC_METER, LITRES), and `DistanceUnit` (KM, MILE).

### 1.2 DTOs (`com.smartlogistics.shared.dto`)
- **Geo-DTOs**: `LocationDTO`, `GeoPoint`, `Coordinates`, and `AddressDTO`.
- **API Response Models**: Standardized models:
  - `ApiResponse<T>`: Success and Creation wrapper.
  - `PageResponse<T>`: Pagination DTO wrapper.
  - `ErrorResponse`: Global generic error model.
  - `ValidationErrorResponse`: Custom validation field failure details.

### 1.3 Annotations & Validators (`com.smartlogistics.shared.validation`)
- Custom Constraint Validators for JSR-380 validation:
  - `@Latitude`: Enforces range [-90, 90].
  - `@Longitude`: Enforces range [-180, 180].
  - `@VehicleNumber`: Enforces regex format: `^[A-Z]{2}[0-9]{2}[A-Z]{1,2}[0-9]{4}$`.
  - `@GstNumber`: Enforces Indian GST format rules.
  - `@PinCode`: Enforces 6 digit PIN format.
  - `@PhoneNumber`: Enforces Indian mobile patterns.

### 1.4 Utilities (`com.smartlogistics.shared.util`)
- **`DistanceCalculator`**: Standard Haversine distance calculator.
- **`GeoUtils`**: Radius lookups and conversion between coordinates and points.
- **`ValidationUtils`**: Static methods to validate capacities, dimensions, coordinates, PAN, and GST.
- **`UuidGenerator`**: Reusable UUID parser.
- **`DateUtils`**: Date/Time parsers and formatters.
- **`PaginationUtils` / `SortingUtils`**: Maps controller page lists and sorting directions.
- **`ResponseBuilder`**: Builders for ResponseEntity.
- **`StorageService` / `MockStorageService`**: Reusable mock file upload abstraction.

### 1.5 Security & Auditing (`com.smartlogistics.shared.security`, `.auditing`)
- **`CurrentUserUtil`**: Resolves audited user details from Spring Security or local ThreadLocal context.
- **`UserAuditFilter`**: Servlet Filter extracting propagated headers (`X-User-Id`) to load `CurrentUserUtil` values.
- **`RoleUtil` / `JwtContextExtractor`**: JWT extracting and RBAC validation.
- **`BaseEntity`**: JPA MappedSuperclass tracking IDs, version, deletion, creation, updates.
- **`SoftDeleteEntity`**: Standard soft delete enabler.

---

## 2. Dependency Inclusion

To use this shared foundation, add this dependency in your service `pom.xml`:

```xml
<dependency>
    <groupId>com.smartlogistics</groupId>
    <artifactId>shared-logistics-library</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```
