# Health Check Feature

This is a Limber feature module that defines a health check endpoint.
A server may use this endpoint to easily implement its own health checks.

### Implementation

1. Add a dependency on this module.
1. Create a `HealthCheckService` implementation.
    ```kotlin
    internal class HealthCheckServiceImpl @Inject constructor() : HealthCheckService {
      override fun healthCheck(): HealthCheckModel {
        healthTry("custom thing", ::customThingCheck)?.let { return@healthCheck it }
        return HealthCheckModel.Healthy
      }
    
      private fun customThingCheck() {
        TODO("Throw an exception if unhealthy")
      }
    }
    ```
1. Add the `HealthCheckModule` Limber module to the list of application modules.
