# Tracking Service

The **Tracking Service** is a clean-architecture microservice responsible for real-time GPS telemetry, checkpoint updates, delay reporting, fuel logs, and state transitions of transport trips.

---

## 1. Architecture

The microservice follows a standard hexagonal/clean-architecture layout:
- **API/Controller Layer**: Exposes REST interfaces validated using Spring Security JWT context headers.
- **Service Layer**: Coordinates transitions using the **State Pattern** and calculates dynamic ETAs using the **Strategy Pattern**.
- **Data Layer (PostgreSQL)**: Persists entities utilizing soft delete (`@SQLRestriction`) and optimistic locking version controls.
- **Messaging (Kafka)**: Consumes status/match events and publishes telemetry/trip updates.

---

## 2. Trip Lifecycle & State Machine

Trips progress through a strict state machine validated by concrete class implementations of `TripState`:

```
   WAITING (Default)
      │
      ▼
   ASSIGNED (Driver & Truck associated)
      │
      ▼
    READY (Pre-trip checks completed)
      │
      ▼
   STARTED (Trip begins, departure timestamp logged)
      │
      ▼ (First GPS coordinate received)
  IN_PROGRESS <───┐
      │           │
      ▼           │
   PAUSED ────────┘ (Break/delay resolved)
      │
      ├─────────────────┬─────────────────┐
      ▼                 ▼                 ▼
  COMPLETED         CANCELLED          DELAYS
(Terminal state)  (Terminal state)
```

- **Transitions**:
  - `assign()`: Transition WAITING -> ASSIGNED.
  - `ready()`: Transition ASSIGNED -> READY.
  - `start()`: Transition READY -> STARTED.
  - `pause()`: Transition STARTED/IN_PROGRESS/RESUMED -> PAUSED.
  - `resume()`: Transition PAUSED -> RESUMED (which then transitions to IN_PROGRESS on GPS signal).
  - `complete()`: Transition STARTED/IN_PROGRESS/RESUMED -> COMPLETED.
  - `cancel()`: Transition active states -> CANCELLED.

---

## 3. Database Schema

The database for the service is `tracking_db`. Entities extend `BaseEntity` (soft delete, `version`, `createdAt`, `updatedAt` audit fields):

1. **Trip**: Primary record linking shipment, driver, truck, business owners, and status.
2. **TripRoute**: planned start/end coordinates and planning distance/duration.
3. **TripCheckpoint**: Sequenced route markers (PICKUP, LOADING, WAYPOINT, DESTINATION).
4. **GpsLocation**: Time-series list of recorded positions (latitude, longitude, speed, heading, altitude).
5. **TripEvent**: Audit events logged chronologically.
6. **FuelLog**: Refueling cost, volume, station name, and odometer.
7. **RestStop**: Start/end timestamps of driver resting periods.
8. **TripDelay**: Duration and reasons of delays en route.
9. **TripSummary**: Stats computed upon completing a trip (total fuel, delay durations, average speed).
10. **TripTimeline**: Sequenced historical replays.

---

## 4. Kafka Events

### Consumer Topics
- `bid.accepted` (Type: `MatchAcceptedEvent`): Triggers auto-creation of trips in ASSIGNED state.
- `shipment.cancelled` (Type: `ShipmentStatusChangedEvent`): Auto-cancels active trips.
- `truck.availability.changed` (Type: `TruckAvailabilityChangedEvent`): Audits truck state.

### Producer Topics
- `trip.created` (Type: `TripCreatedEvent`)
- `trip.started` (Type: `TripStartedEvent` from common library)
- `trip.paused` (Type: `TripPausedEvent`)
- `trip.resumed` (Type: `TripResumedEvent`)
- `trip.completed` (Type: `TripCompletedEvent` from common library)
- `trip.cancelled` (Type: `TripCancelledEvent`)
- `gps.updated` (Type: `GpsUpdatedEvent`)
- `eta.updated` (Type: `EtaUpdatedEvent`)

---

## 5. Feign Integrations

Calls external microservices inside permitAll endpoints to validate entity existences before creating or updating trips:
- **`ShipmentClient`**: Validates shipment exists and is active. Exposes `/internal/shipments/{id}` and `/api/v1/shipments/{id}`.
- **`TruckClient`**: Validates truck state via `/internal/trucks/{id}`.
- **`UserClient`**: Validates driver license and business owner accounts via `/internal/users/...`.

---

## 6. Testing Guide

Execute tests verifying compilation, strategies, and state patterns:
```bash
mvn clean test
```

Key test components:
- `TripStateTest`: Verifies correct state machine transition matrices.
- `EtaStrategyTest`: Verifies Haversine and elapsed average-speed math.
- `GpsValidationTest`: Coordinates and speed boundaries sanity audits.
- `TripServiceTest`: Mockito mock verification.
- `TripControllerTest`: MockMvc endpoint authentication role tests.

---

## 7. Deployment Guide

Build the Docker container:
```bash
docker build -t tracking-service:latest .
```

Run container:
```bash
docker run -p 8086:8086 -e SPRING_PROFILES_ACTIVE=prod tracking-service:latest
```
