# Complete REST API Contract
**Project:** Smart Logistics Optimization Platform
**Version:** 1.0.0
**Style:** OpenAPI 3.1 Concepts & Schemas

---

## 1. Global API Standards

### 1.1 Base URL & Routing
All APIs will be routed through the API Gateway.
*   **Gateway Base URL:** `https://api.smartlogistics.com`
*   **Service Routes:** `/api/v1/<resource>`

### 1.2 API Naming Rules
*   Use **nouns** (pluralized) instead of verbs (e.g., `/api/v1/users`, not `/api/v1/getUser`).
*   Nested resources for relationships (e.g., `/api/v1/trucks/{truckId}/documents`).
*   Action endpoints use trailing actions (e.g., `/api/v1/shipments/{shipmentId}/cancel`).

### 1.3 Security & Authentication
*   **JWT Authentication:** All protected routes require an `Authorization: Bearer <token>` header.
*   **Public APIs:** Login, Register, Forgot Password, Verify Email, Webhooks.
*   **Protected APIs:** Any profile, shipment, or tracking CRUD operations.
*   **Admin APIs:** All routes under `/api/v1/admin` require `ROLE_ADMIN`.

### 1.4 File Uploads Standard
*   **Maximum Size:** 5MB for images, 10MB for PDFs/documents.
*   **Allowed Formats:** `image/jpeg`, `image/png`, `application/pdf`.
*   **Headers:** `Content-Type: multipart/form-data`.
*   **Validation:** Signature matching (magic bytes check) in the Gateway/Service.

---

## 2. Universal Response Models

### 2.1 Success Response Format
Every successful 2xx response must follow this envelope structure.

```json
{
  "success": true,
  "message": "Resource fetched successfully",
  "data": { ... }, // Or Array [...]
  "pagination": { // Included only for list endpoints
    "page": 0,
    "size": 20,
    "totalElements": 150,
    "totalPages": 8,
    "last": false
  },
  "metadata": {
    "timestamp": "2026-07-02T10:00:00Z"
  }
}
```

### 2.2 Error Response Format
Universal model for 4xx and 5xx errors.

```json
{
  "success": false,
  "timestamp": "2026-07-02T10:05:00Z",
  "status": 400,
  "errorCode": "VALIDATION_FAILED",
  "message": "Invalid request payload",
  "path": "/api/v1/trucks",
  "traceId": "abc123xyz890",
  "validationErrors": [
    {
      "field": "plateNumber",
      "message": "Plate number is required"
    }
  ]
}
```

---

## 3. Global Query Parameters (Search, Filter, Pagination)

For any endpoint returning a list (e.g., `GET /api/v1/shipments`), support the following queries:

| Parameter | Type | Default | Description |
| :--- | :--- | :--- | :--- |
| `page` | Integer | `0` | Zero-based page index. |
| `size` | Integer | `20` | Number of items per page. |
| `sort` | String | `createdAt,desc` | Sorting criteria (`field,direction`). |
| `search` | String | null | Global text search (e.g., across name, title). |
| `startDate` | ISO8601 | null | Filter range start (createdAt or custom field). |
| `endDate` | ISO8601 | null | Filter range end. |
| `lat` & `lng` | Double | null | Used with `radius` for location-based search. |
| `status` | String | null | Generic status filter (e.g., `IN_TRANSIT`). |

---

## 4. Microservices API Endpoints

### 4.1 Auth Service (`/api/v1/auth`)

| Method | Endpoint | Description | Auth Req | Roles |
| :--- | :--- | :--- | :--- | :--- |
| POST | `/register` | Register a new user | No | - |
| POST | `/login` | Authenticate user and issue tokens | No | - |
| POST | `/refresh` | Get new access token using refresh token | No | - |
| POST | `/logout` | Invalidate current refresh token | Yes | ANY |
| POST | `/forgot-password`| Send password reset email | No | - |
| POST | `/reset-password` | Set new password with reset token | No | - |
| POST | `/verify-email` | Verify email address | No | - |
| PUT | `/change-password` | Change password for logged-in user | Yes | ANY |
| GET | `/me` | Get current logged-in user context | Yes | ANY |
| POST | `/roles/assign` | Assign RBAC roles (Admin Only) | Yes | ADMIN |

**Example Request: POST `/api/v1/auth/login`**
```json
{
  "email": "driver@example.com",
  "password": "SecurePassword123!"
}
```

---

### 4.2 User Service (`/api/v1/users`)

| Method | Endpoint | Description | Auth Req | Roles |
| :--- | :--- | :--- | :--- | :--- |
| GET | `/` | Search/Paginate users | Yes | ADMIN |
| GET | `/{id}` | Get user profile by ID | Yes | ANY |
| PUT | `/{id}` | Update basic user profile | Yes | SELF, ADMIN |
| GET | `/{id}/addresses` | List addresses | Yes | SELF, ADMIN |
| POST | `/{id}/addresses` | Add a new address | Yes | SELF, ADMIN |
| PUT | `/{id}/driver-profile`| Update driver specific profile | Yes | DRIVER |
| PUT | `/{id}/business-profile`| Update business specific profile | Yes | SHIPPER |
| POST | `/{id}/documents` | Upload KYC/License documents | Yes | SELF |
| POST | `/{id}/emergency-contacts`| Add emergency contact | Yes | SELF |

**File Upload Example: POST `/api/v1/users/{id}/documents`**
*   **Request Type:** `multipart/form-data`
*   **Body:** `file` (Binary), `documentType` (String: "LICENSE", "ID")

---

### 4.3 Truck Service (`/api/v1/trucks`)

| Method | Endpoint | Description | Auth Req | Roles |
| :--- | :--- | :--- | :--- | :--- |
| GET | `/` | Search/Paginate trucks | Yes | ANY |
| POST | `/` | Register a new truck | Yes | OWNER, DRIVER |
| GET | `/{id}` | Get truck details | Yes | ANY |
| PUT | `/{id}` | Update truck details | Yes | OWNER |
| GET | `/nearby` | Find nearby available trucks (`?lat=&lng=&radius=`) | Yes | SHIPPER, ADMIN|
| POST | `/{id}/images` | Upload truck images | Yes | OWNER |
| POST | `/{id}/documents` | Upload truck documents (Insurance, Reg) | Yes | OWNER |
| PUT | `/{id}/availability`| Set truck available/booked | Yes | DRIVER, OWNER |
| POST | `/{id}/maintenance` | Log maintenance records | Yes | OWNER |

---

### 4.4 Shipment Service (`/api/v1/shipments`)

| Method | Endpoint | Description | Auth Req | Roles |
| :--- | :--- | :--- | :--- | :--- |
| GET | `/` | List shipments (supports advanced filtering)| Yes | ANY |
| POST | `/` | Create a new shipment request | Yes | SHIPPER |
| GET | `/{id}` | Get shipment details | Yes | ANY |
| PUT | `/{id}` | Update shipment (if not assigned) | Yes | SHIPPER |
| DELETE | `/{id}` | Cancel a shipment | Yes | SHIPPER |
| POST | `/{id}/items` | Add items to shipment | Yes | SHIPPER |
| POST | `/{id}/documents` | Upload BOL, invoices | Yes | ANY |
| GET | `/categories` | List available shipment categories | Yes | ANY |

**Example Request: POST `/api/v1/shipments`**
```json
{
  "title": "Electronics load to NY",
  "pickupAddressId": "uuid-1",
  "dropAddressId": "uuid-2",
  "pickupTime": "2026-07-05T10:00:00Z",
  "truckTypeRequired": "DRY_VAN"
}
```

---

### 4.5 Matching Service (`/api/v1/matches`)

| Method | Endpoint | Description | Auth Req | Roles |
| :--- | :--- | :--- | :--- | :--- |
| POST | `/driver-requests` | Driver specifies route preferences | Yes | DRIVER |
| GET | `/ai/recommendations/trucks` | Get AI recommended trucks for a shipment| Yes | SHIPPER |
| GET | `/ai/recommendations/loads` | Get AI recommended loads for a driver | Yes | DRIVER |
| POST | `/bids` | Driver places a bid on a shipment | Yes | DRIVER |
| GET | `/bids` | Shipper views bids for their shipment | Yes | SHIPPER |
| POST | `/bids/{id}/accept` | Shipper accepts a bid | Yes | SHIPPER |
| POST | `/bids/{id}/reject` | Shipper rejects a bid | Yes | SHIPPER |

---

### 4.6 Tracking Service (`/api/v1/tracking`)

| Method | Endpoint | Description | Auth Req | Roles |
| :--- | :--- | :--- | :--- | :--- |
| POST | `/trips` | Create trip (Internal/Webhook called) | Yes | ADMIN/SYSTEM|
| GET | `/trips/{id}` | Get current trip info and status | Yes | ANY |
| POST | `/trips/{id}/start` | Driver starts the trip | Yes | DRIVER |
| POST | `/trips/{id}/end` | Driver marks trip as delivered | Yes | DRIVER |
| POST | `/trips/{id}/gps` | Upload batch of GPS coordinates | Yes | DRIVER |
| GET | `/trips/{id}/route` | Get planned vs actual route (Polylines)| Yes | ANY |
| POST | `/trips/{id}/events`| Log an event (Traffic, Breakdown) | Yes | DRIVER |
| POST | `/trips/{id}/fuel` | Log fuel consumption and cost | Yes | DRIVER |

---

### 4.7 Notification Service (`/api/v1/notifications`)

| Method | Endpoint | Description | Auth Req | Roles |
| :--- | :--- | :--- | :--- | :--- |
| GET | `/` | List user's notifications | Yes | SELF |
| PUT | `/{id}/read` | Mark notification as read | Yes | SELF |
| DELETE | `/{id}` | Delete a notification | Yes | SELF |
| PUT | `/preferences` | Update email/SMS/Push preferences | Yes | SELF |

---

### 4.8 Review Service (`/api/v1/reviews`)

| Method | Endpoint | Description | Auth Req | Roles |
| :--- | :--- | :--- | :--- | :--- |
| POST | `/` | Submit a review/rating | Yes | ANY |
| GET | `/` | Get reviews for a driver/shipper | Yes | ANY |
| PUT | `/{id}` | Edit a review | Yes | SELF |
| DELETE | `/{id}` | Delete a review | Yes | SELF, ADMIN |
| POST | `/{id}/replies` | Reply to a review | Yes | SELF |
| POST | `/{id}/report` | Report an inappropriate review | Yes | ANY |

---

### 4.9 Admin Service (`/api/v1/admin`)

| Method | Endpoint | Description | Auth Req | Roles |
| :--- | :--- | :--- | :--- | :--- |
| GET | `/dashboard/metrics`| Get overall platform KPI metrics | Yes | ADMIN |
| GET | `/users` | Advanced user management/filtering | Yes | ADMIN |
| POST | `/users/{id}/block` | Block a user account | Yes | ADMIN |
| GET | `/audit-logs` | Query system audit logs | Yes | ADMIN |
| GET | `/reports` | Generate CSV/PDF system reports | Yes | ADMIN |
| PUT | `/settings` | Update global system settings | Yes | ADMIN |

---

### 4.10 Analytics Service (`/api/v1/analytics`)

| Method | Endpoint | Description | Auth Req | Roles |
| :--- | :--- | :--- | :--- | :--- |
| GET | `/revenue` | Get revenue stats (Shipper/Driver/System)| Yes | ANY |
| GET | `/trips/stats` | Trip performance statistics | Yes | ANY |
| GET | `/sustainability` | Get fuel savings & carbon reduction metrics| Yes | ANY |
| POST| `/reports/business` | Generate analytics report for Business | Yes | SHIPPER |
| POST| `/reports/driver` | Generate analytics report for Driver | Yes | DRIVER |

---

## 5. OpenAPI 3.1 Schemas (Common Domain Models)

**User Schema**
```yaml
User:
  type: object
  properties:
    id:
      type: string
      format: uuid
    email:
      type: string
      format: email
    phone:
      type: string
    status:
      type: string
      enum: [ACTIVE, BLOCKED, PENDING]
    roles:
      type: array
      items:
        type: string
```

**Truck Schema**
```yaml
Truck:
  type: object
  properties:
    id:
      type: string
      format: uuid
    ownerId:
      type: string
      format: uuid
    plateNumber:
      type: string
    make:
      type: string
    model:
      type: string
    year:
      type: integer
    type:
      type: string
      enum: [FLATBED, REEFER, DRY_VAN]
```

**Shipment Schema**
```yaml
Shipment:
  type: object
  properties:
    id:
      type: string
      format: uuid
    title:
      type: string
    status:
      type: string
      enum: [CREATED, ASSIGNED, IN_TRANSIT, DELIVERED]
    pickupTime:
      type: string
      format: date-time
    pricing:
      $ref: '#/components/schemas/Pricing'
```
