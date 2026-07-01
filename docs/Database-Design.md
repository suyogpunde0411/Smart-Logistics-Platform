# Database Design

## 1. Identifiers
- Use **UUIDs** (specifically UUID v4) as Primary Keys across all database tables. Avoid auto-incrementing integer IDs to ensure global uniqueness and make the system robust for distributed operations and potential future sharding.

## 2. Deletion Strategy
- All tables must implement **Soft Delete**.
- Add a `deleted_at` timestamp column and an `is_deleted` boolean to enable data preservation and compliance, without permanently removing records from the disk.

## 3. Auditing
- All tables must contain audit fields:
  - `created_at` (Timestamp)
  - `updated_at` (Timestamp)
  - `created_by` (UUID string representing user)
  - `updated_by` (UUID string representing user)

## 4. Microservice Isolation
- Follow the Database-per-Service pattern.
- No direct database connections across microservice boundaries. Data needed by another service must be retrieved via REST APIs or asynchronous Kafka events.
