package limber.service.healthCheck

import com.google.inject.Inject
import limber.client.healthCheck.HealthCheckClient
import limber.model.healthCheck.HealthCheck

internal class TestHealthCheckService @Inject constructor(
  healthCheckClient: HealthCheckClient,
) : HealthCheckService(healthCheckClient) {
  override val healthChecks: Map<String, HealthCheck> =
    mapOf(
      "http" to HealthCheck(::httpHealthCheck),
      "server" to HealthCheck(::serverHealthCheck),
    )
}
