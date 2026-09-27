# Transaction Monitoring Prototype

A small prototype of a transaction monitoring service, built to practise designing and running a service with containers, Kubernetes and OAuth2/OIDC.

## What it does

- Accepts payment transactions through a REST API
- Runs each transaction through detection rules (currently a large-amount threshold rule)
- Stores transactions and the alerts they raise in PostgreSQL

## Architecture

```
Client ──HTTP/JSON──▶ Spring Boot API ──▶ Rules ──▶ PostgreSQL
                                                    (transactions, alerts)
```

| Layer | Technology |
|---|---|
| API | Java 21, Spring Boot, Spring Web, Bean Validation |
| Persistence | Spring Data JPA, PostgreSQL 17, Flyway migrations |
| Packaging | Multi-stage Docker build, Docker Compose |
| Operations | Spring Boot Actuator health endpoints |

## Run it

```bash
docker compose up --build -d
curl http://localhost:8080/actuator/health
```

## API

| Method | Path | Purpose |
|---|---|---|
| POST | `/api/transactions` | Submit a transaction. Returns 201 with any alerts raised, or 409 if the reference was already submitted |
| GET | `/api/transactions/{id}` | Fetch a transaction and its alerts |
| GET | `/api/alerts?status=OPEN` | List alerts by status, newest first |

Example:

```bash
curl -s -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -d '{"transactionRef":"TX-1002","accountId":"GB29NWBK60161331926819",
       "amount":15000.00,"currency":"GBP","direction":"DEBIT",
       "bookedAt":"2026-09-27T10:20:00Z"}'
```

## Design notes

- **Money is stored as `NUMERIC(19,4)`**, never floating point, to avoid rounding errors.
- **Safe to reprocess:** `transaction_ref` is unique, and each rule can raise at most one alert per transaction. This matters once transactions arrive through Kafka, which can deliver the same message more than once.
- **Flyway owns the schema.** Hibernate never changes the database.
- **Rules are pluggable.** Each rule implements `MonitoringRule`, and Spring injects all of them into the service, so adding a rule doesn't change existing code.
- **Configuration comes from environment variables**, so the same image runs locally, in Docker Compose and in Kubernetes.
- **The runtime image is small and runs as a non-root user.**

## Roadmap

- [x] REST API, PostgreSQL schema, Docker Compose
- [ ] Deploy to Kubernetes (kind) with a ConfigMap, a Secret and health probes
- [ ] Secure the API with OAuth2/OIDC using Keycloak
- [ ] Stream transactions through Kafka and add a velocity rule
