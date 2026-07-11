# Review & Rating Service

The **Review & Rating Service** manages driver, business, and fleet owner reviews, calculates trust and reputation scores utilizing GoF Strategy design patterns, moderates reviews, and updates profile metrics across the smart logistics microservice architecture.

---

## 1. Architecture

The service follows clean architecture guidelines:
* **API Controllers**: Exposes REST interfaces to perform CRUD reviews, likes, flags, disputes, and score lookups. Enforces security claims filters.
* **Core Services**: Orchestrates business logic validations, coordinates MapStruct converters, and evaluates trust score equations.
* **Strategies**: Interface `TrustScoreCalculationStrategy` with `WeightedTrustScoreStrategy` implementing a weighted formula to calculate scores from 0-100.
* **Downstream Feign Mappings**: Intercepts outgoing requests to verify trip completions via `tracking-service` and user status via `user-service`.

---

## 2. Review Flow

```
                      [Submit CreateReview Request]
                                   │
                                   ▼
                   [Validate User Exists (Feign lookup)]
                                   │
                  [Validate Trip COMPLETED (Feign lookup)]
                                   │
            [Reconcile Reviewer/Reviewee ownership on Trip]
                                   │
                    [Verify Duplicate Checks (Max 1)]
                                   │
                                   ▼
                           [Persist Review]
                                   │
                     ┌─────────────┴─────────────┐
                     ▼                           ▼
           [Recalculate Averages]     [Recalculate Trust Score]
                     │                           │
                     ▼                           ▼
            (RatingStatistics)             (TrustScore)
                     │                           │
                     └─────────────┬─────────────┘
                                   │
                                   ▼
                    [Publish event `review.created`]
```

---

## 3. Trust Score Calculation

The default reputation engine utilizes a **Weighted Scoring Algorithm** with clamping:
* **Base Score**: Starts at `100`.
* **Rating Average**: Subtracts `(5.0 - overallAverage) * 10` points.
* **Trips Bonus**: Adds `+0.5` points per completed trip (capped at maximum bonus of `+15` points).
* **Cancellation Penalty**: Subtracts `-2` points per trip cancellation.
* **Reports Penalty**: Subtracts `-5` points per flagged reported review.
* **Dispute Penalty**: Subtracts `-10` points per review dispute.
* **Result Range**: Clamped between `0` (lowest trust) and `100` (highest trust).

---

## 4. Kafka Topics

### Consumers
* `trip.completed` -> updates completed trips count.
* `trip.cancelled` -> updates cancelled trips count.
* `user.deleted` -> soft-deletes reviews where the deleted user is the reviewee.

### Producers
* `review.created` (Payload: `ReviewCreatedEvent`)
* `review.updated` (Payload: `ReviewUpdatedEvent`)
* `review.reported` (Payload: `ReviewReportedEvent`)
* `trust-score.updated` (Payload: `TrustScoreUpdatedEvent`)

---

## 5. Feign Integrations
1. **User Service** (`user-service`):
   * Endpoints: `/internal/users/{id}`
   * Purpose: Validating profile existence prior to letting users write reviews.
2. **Tracking Service** (`tracking-service`):
   * Endpoints: `/api/v1/trips/{id}`
   * Purpose: Verifying trip completion status and participant driver/business owner matching.

---

## 6. Testing Guide

Execute tests via:
```bash
mvn clean test
```
* **TrustScoreStrategyTest**: Tests weighted deduction math and clamping boundaries.
* **ReviewServiceTest**: Verifies 24-hours immutability checks, duplicate submission blocks, and feign validations.
* **ReviewControllerTest**: Asserts stateless JWT filter authorization checks.

---

## 7. Deployment Guide

Build package and construct Docker image:
```bash
mvn clean package -DskipTests
docker build -t review-service:latest .
```

Run container:
```bash
docker run -p 8088:8088 -e SPRING_PROFILES_ACTIVE=prod review-service:latest
```
