# Notification Service

The **Notification Service** is an event-driven, clean-architecture microservice responsible for sending transactional emails, SMS, mobile push alerts, and in-app alerts across the logistics platform.

---

## 1. Architecture

The service follows clean architecture patterns:
* **API Layer**: Exposes secure REST endpoints validated via gateway JWT claims headers. Exposes a Feign-compatible endpoint `/internal/notifications`.
* **Core Service Layer**: Translates events into alerts, applies user preference rules, and maps dispatches using the **Strategy Pattern** (e.g. `NotificationChannelStrategy` interface) and **Factory Pattern** for provider resolution.
* **Retry Engine**: Outbox tables capture delivery failures and dynamically calculate exponential backoff limits.

---

## 2. Notification Flow

```
                      [Kafka Event / Feign Call]
                                  │
                                  ▼
                     [Resolve User Preference]
                                  │
                       Is Channel Allowed?
                       ├── NO  ──> [Ignored / Log]
                       └── YES ──> [Resolve Template Engine Placeholders]
                                              │
                                              ▼
                             [Notification Dispatch Strategy]
                                              │
                                     Did Dispatch Succeed?
                                     ├── YES ──> [Save Log (SENT)] ──> Publish Kafka `notification.sent`
                                     └── NO  ──> [Save Log (FAILED)] ──> Enqueue in Outbox Queue
                                                                                      │
                                                                                      ▼
                                                                     [Background Queue Processor Retry]
                                                                      (Attempts retry up to 5 times)
                                                                                      │
                                                                            Exceeded 5 retries?
                                                                            ├── NO  ──> Exponential Backoff retry
                                                                            └── YES ──> Move to Dead Letter Queue (DLQ) ──> Publish Kafka `notification.failed`
```

---

## 3. Kafka Topics

### Consumers
* `user-registration` -> Seeding default user preferences and welcome/verification email flow.
* `user-events` -> Syncing profile updates.
* `truck.registered` -> Sending registration confirmation.
* `shipment.created` -> Notifying owner of shipment logging.
* `match.created` -> Sending match notification.
* `bid.placed` -> Notifying business owners of bid amount.
* `bid.accepted` -> Sending matched and price metrics.
* `trip.started` / `trip.completed` / `trip.cancelled` -> Pushing real-time status alerts.
* `review-events` -> Auditing post-trip reviews.

### Producers
* `notification.sent` (Payload: `NotificationSentEvent`)
* `notification.failed` (Payload: `NotificationFailedEvent`)

---

## 4. Supported Channels
1. **Email**: Spring MailSender SMTP gateway.
2. **SMS**: SMS provider interface (backed by Mock SMS console logs).
3. **Push**: Simulated Firebase Cloud Messaging (FCM) interface.
4. **In-App**: Staged in database table for polling.
5. **WhatsApp**: Interface skeleton only for future releases.

---

## 5. Retry Strategy
* **Transactional Outbox Queuing**: Outbox entities (`EmailQueue`, `SmsQueue`, `PushQueue`) track retry times.
* **Exponential Backoff**: Calculation: `Base Delay (1000ms) * Multiplier (2.0)^attempts`.
* **DLQ Threshold**: Max limit of 5 retry cycles before transitioning state to `DLQ` and dispatching a `notification.failed` topic payload.

---

## 6. Template System
Default templates are automatically initialized in `notification_templates` at bootstrap:
* Welcome Email
* Email Verification
* Password Reset
* Truck Registered
* Shipment Created
* Shipment Matched
* Bid Received
* Bid Accepted
* Trip Started
* Trip Completed
* Trip Cancelled
* Document Expiring
* Insurance Expiring
* Maintenance Reminder
* Review Request

Placeholders formatted as `${key}` are interpolated using standard key-value map replacement rules.

---

## 7. Testing Guide
Execute tests:
```bash
mvn clean test
```
* Key test classes:
  * `PreferenceServiceTest`: Validates permission check matrices.
  * `TemplateEngineTest`: Verifies parser string replacement.
  * `NotificationServiceTest`: Checks strategy routing and retry-queues.
  * `NotificationControllerTest`: Asserts MockMvc endpoint authentication.

---

## 8. Deployment Guide

Build jar and package container:
```bash
mvn clean package -DskipTests
docker build -t notification-service:latest .
```

Run container:
```bash
docker run -p 8087:8087 -e SPRING_PROFILES_ACTIVE=prod notification-service:latest
```
