# Analytics Service

The **Analytics Service** is a high-performance, read-optimized business intelligence (BI) microservice that consumes platform-wide events to generate real-time metrics, role-based dashboards, carbon savings records, and audit reports.

---

## 1. Architecture

Following clean architecture guidelines, the service is structured as follows:
- **Presentation Layer**: Exposes secure REST endpoints `/api/v1/analytics/dashboard/*` and `/api/v1/analytics/reports/*` to build summaries and report datasets.
- **Factory Pattern (Dashboards)**: Instantiates role-based builders (`AdminDashboardBuilder`, `DriverDashboardBuilder`, `BusinessDashboardBuilder`, `FleetDashboardBuilder`) via `DashboardBuilderFactory` to cache summaries in Redis for up to 10 minutes.
- **Strategy Pattern (Reports)**: Employs different strategies (`RevenueReportGenerator`, `TripReportGenerator`, `TruckReportGenerator`, etc.) implementing `ReportGeneratorStrategy` to build structural data rows and exports (CSV/PDF bytes).
- **Read-Only Ingestion**: Does not manage direct transactions or write to external service databases. It populates `analytics_db` strictly by consuming Kafka events.

---

## 2. Analytics Flow

```
   [Kafka Event Ingestion]
              │
              ▼
   [AnalyticsKafkaConsumer]
              │
   ┌──────────┴──────────┐
   ▼                     ▼
[Update Repositories] [Invalidate Redis Cache ('dashboards')]
   │
   ▼
[analytics_db Schema]
   │
   ▼
[DashboardBuilder / ReportStrategy]
   │
   ▼
[API Gateway / Endpoints]
```

---

## 3. Kafka Consumers

The service consumes the following 12 topics:
- `user-registration` -> Synchronizes `DriverAnalytics`, `BusinessAnalytics`, and `FleetAnalytics`.
- `truck.registered` -> Aggregates fleet size metrics.
- `shipment.created` -> Tracks total volume.
- `shipment.completed` -> Adjusts completed spent logs.
- `match.created` / `bid.accepted` -> Recomputes auto-matching success rate.
- `trip.started` -> Registers active trip logs.
- `trip.completed` -> Triggers revenue additions, fuel logs, and carbon offset calculations.
- `trip.cancelled` -> Registers driver/shipper cancellation rates.
- `review.created` -> Recalculates driver and shipper ratings.
- `trust-score.updated` -> Updates driver trust index metrics.
- `notification.sent` -> Tracks total system outbound alert statistics.

---

## 4. Dashboard APIs

### Fetch Summary
`GET /api/v1/analytics/dashboard/summary`
- Secured. Auto-detects caller role (`ADMIN`, `DRIVER`, `BUSINESS_OWNER`, `FLEET_OWNER`) and returns custom charts and KPI cards.

### Search Trips
`GET /api/v1/analytics/dashboard/trips`
- Supports sorting, pagination, and multi-criteria filters (`driverId`, `businessId`, `city`, `startDate`, etc.). Enforces driver isolation.

### Strategic Reports
`POST /api/v1/analytics/reports/generate`
`POST /api/v1/analytics/reports/export`
- Generates JSON summary reports or returns CSV/PDF file downloads.

---

## 5. Database Schema

All entities extend `BaseEntity` providing soft-delete (`isDeleted = false`) and standard audit fields:
- `dashboard_metrics`: Flat key-value table.
- `driver_analytics`: Tracks revenue, durations, speeds, and late deliveries per driver.
- `business_analytics`: Tracks shipment budgets and totals per shipper.
- `fleet_analytics`: Tracks fleet utilization rates and empty trip reductions.
- `truck_analytics`: Tracks total mileage, active hours, and fuel consumption per truck.
- `trip_analytics`: Detailed trip log linked with custom left-joins to `shipment_analytics`.

---

## 6. Testing Guide

Run the test suite using:
```bash
mvn clean test
```
The test suite validates:
- Kafka consumer ingestion correctness.
- Factory patterns and Builder chains.
- Strategy calculations and format exports.

---

## 7. Deployment Guide

Construct the Docker image:
```bash
mvn clean package -DskipTests
docker build -t analytics-service:latest .
```

Run the container:
```bash
docker run -p 8089:8089 -e SPRING_PROFILES_ACTIVE=prod analytics-service:latest
```
