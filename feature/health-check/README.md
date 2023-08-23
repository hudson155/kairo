# Health Check Feature

Adding `HealthCheckFeature` to a Server creates 2 health check endpoints.

- `GET /health/liveness`,
  which should respond with `200 OK`
  as soon as possible after server startup.
- `GET /health/readiness`,
  which should respond with `200 OK`
  only once the server is ready to receive traffic.

## Usage

The liveness endpoint works automatically
after adding `HealthCheckFeature` to the Server.

The readiness endpoint depends on server implementation.
Create a health check service implementation as follows.

```kotlin
internal class HealthCheckServiceImpl @Inject constructor(
  healthCheckClient: HealthCheckClient,
  private val jdbi: Jdbi,
) : HealthCheckService(healthCheckClient) {
  override val healthChecks: Map<String, HealthCheck> =
    mapOf(
      "http" to HealthCheck(::httpHealthCheck),
      "server" to HealthCheck(::serverHealthCheck),
      "sql" to HealthCheck(::sqlHealthCheck),
    )

  private suspend fun sqlHealthCheck(): HealthCheckRep.State =
    healthyIfNoException {
      val result = jdbi.handle { handle ->
        val query = handle.createQuery("select 1")
        return@handle query.mapTo(Int::class.java).single()
      }
      check(result == 1)
    }
}
```

The `http` and `server` health checks should always be included.
The `sql` health check is an example of how to add another check.
It should be included for servers that have a SQL database.
Any other external resources (such as databases or connection pools)
or internal resources (such as thread pools)
should also have a health check.

Next, bind this health check by passing it to the feature

```kotlin
HealthCheckFeature(HealthCheckServiceImpl::class, config.restClient.baseUrls.self)
```
