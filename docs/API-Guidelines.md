# API Guidelines

## 1. RESTful Principles
- **Nouns over Verbs**: Use nouns for resources (e.g., `/api/v1/shipments` instead of `/api/v1/getShipments`).
- **HTTP Methods**:
  - `GET` - Retrieve resources.
  - `POST` - Create resources.
  - `PUT` - Fully update resources.
  - `PATCH` - Partially update resources.
  - `DELETE` - Remove resources.

## 2. API Response Wrapper
All API responses must follow a consistent, standardized format using the `ApiResponse` wrapper located in `common-library`:

```json
{
  "success": true,
  "message": "Resource retrieved successfully",
  "data": { ... },
  "timestamp": "2024-01-01T12:00:00Z"
}
```

For errors:
```json
{
  "success": false,
  "message": "Validation failed",
  "errors": ["Field 'email' must be valid"],
  "timestamp": "2024-01-01T12:00:00Z"
}
```

## 3. Pagination, Sorting & Filtering
- Endpoints returning lists must support pagination (`page`, `size`) and sorting (`sort=field,desc`).
- Standard query parameters should be used for filtering.

## 4. Versioning
APIs must be versioned in the URL path: `/api/v1/...`
