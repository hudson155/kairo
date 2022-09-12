package limber.service

import com.google.inject.Inject
import limber.client.HealthCheckClient
import limber.model.HealthCheck

internal class TestHealthCheckService @Inject constructor(
  healthCheckClient: HealthCheckClient,
) : HealthCheckService(healthCheckClient) {
  override val healthChecks: Map<String, HealthCheck> = mapOf(
    "http" to HealthCheck(::httpHealthCheck),
    "server" to HealthCheck(::serverHealthCheck),
  )
}
