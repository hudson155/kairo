# `kairo-health-check-feature`

Kairo's health check Feature configures liveness and readiness health check REST endpoints:

- `GET /health/liveness` is designed to always respond with `200 OK` and no response body.
  This can be used to check if the Server is running.
  The Server may or may not be ready to receive traffic.
- `GET /health/readiness` is designed to run custom health checks,
  and respond with either `200 OK` or `500 Internal Server Error`
  depending on the status of those health checks.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  implementation("kairo:kairo-health-check-feature:$kairoVersion")
}
```

### Step 2: Implement `HealthCheckService`

```kotlin
// src/main/kotlin/yourPackage/server/monolith/MonolithHealthCheckService.kt

internal class MonolithHealthCheckService @Inject constructor() : HealthCheckService() {
  override val healthChecks: Map<String, HealthCheck> =
    mapOf(
      "server" to HealthCheck(::serverHealthCheck),
      "custom" to HealthCheck(::customHealthCheck),
    )

  private fun customHealthCheck(): HealthCheckRep.Status {
    TODO("Your custom health check.")
  }
}
```

### Step 3: Create the Feature

```kotlin
// src/main/kotlin/yourPackage/server/monolith/MonolithServer.kt

KairoHealthCheckFeature(MonolithHealthCheckService::class)
```
