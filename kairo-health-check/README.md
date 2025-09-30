# Health Check

With just a few lines of code
you can add health check endpoints to your Kairo application.

```kotlin
val healthChecks = mapOf(
  "sql" to HealthCheck { SqlFeature.healthCheck(koin) },
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
Throw an exception to fail the check.

```kotlin
val healthChecks = mapOf(
  "sql" to HealthCheck { SqlFeature.healthCheck(koin) },
  "custom" to HealthCheck {
    // ...
  },
)

val features = listOf(
  HealthCheckFeature(healthChecks),
)
```

### Built-in health checks

Depending on which other Framework Features you're using,
you might get other health checks for free.

- SQL Feature
  ```kotlin
  HealthCheck { SqlFeature.healthCheck(koin) }
  ```

### Advanced Feature configuration

The Health Check Feature has a few more options you can configure.

- `includeDefaultHealthCheck` is `true` by default.
  This check ensures that the Server is running. If the server is still starting, or has begun stopping, it will fail.
- `timeout` is 2 seconds by default.
  You can adjust this if needed.
