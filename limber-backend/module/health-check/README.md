# `limber-backend:module:health-check`

This is a Limber module that defines a health check endpoint.
An application may use this endpoint to easily implement its own health checks.

### Implementation

1. Add a dependency on this module.
1. Add the `HealthCheckModule` Limber module to the list of application modules.
1. Implement and bind a `HealthCheckService` implementation.

### `HealthCheckService`

A simple example of a `HealthCheckService` implementation follows.

```kotlin
internal class HealthCheckServiceImpl @Inject constructor() : HealthCheckService {
  override fun healthCheck(): HealthCheckModel {
    someCustomCheck()?.let { return it }
    return HealthCheckModel.HealthyHealthCheckModel
  }

  private fun someCustomCheck(): HealthCheckModel.UnhealthyHealthCheckModel? {
    TODO("Return UnhealthyHealthCheckModel if unhealthy.")
    return null
  }
}
```
