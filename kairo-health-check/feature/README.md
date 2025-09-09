# Kairo Health Check Feature

With just a few lines of code
you can add health check endpoints to your Kairo application.

```kotlin
val healthChecks = mapOf(
  "sql" to HealthCheck { SqlFeature.healthCheck(koinApplication.koin.get()) },
  "custom" to HealthCheck {
    // ...
  },
)
```

## Installation

Install `kairo-health-check-feature`.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-health-check-feature")
}
```

## Usage

For basic usage, just add `HealthCheckFeature()`.

```kotlin
val features = listOf(
  HealthCheckFeature(),
)
```

This creates 2 endpoints.

- `GET /health/liveness` returns 200 OK if the server is running.
- `GET /health/readiness` returns 200 OK if all health checks pass.

### Customizing health checks

More likely, you're going to want to customize the readiness checks.
Implement the `HealthCheck` functional interface.

```kotlin
val healthChecks = mapOf(
  "sql" to HealthCheck { SqlFeature.healthCheck(koinApplication.koin.get()) },
  "custom" to HealthCheck {
    // ...
  },
)

val features = listOf(
  HealthCheckFeature(healthChecks),

  // Domain Feature.
  UserFeature(koinApplication.koin),
)

val server = Server(
  name = "...",
  features = features,
)
```

### Built-in health checks

Depending on which other Framework Features you're using,
you might get other health checks for free.

- SQL Feature
  ```kotlin
  HealthCheck { SqlFeature.healthCheck(koinApplication.koin.get()) }
  ```
