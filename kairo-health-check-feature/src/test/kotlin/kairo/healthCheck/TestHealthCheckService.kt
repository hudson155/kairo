package kairo.healthCheck

import com.google.inject.Inject

internal class TestHealthCheckService @Inject constructor() : HealthCheckService() {
  internal var testStatus: HealthCheckRep.Status = HealthCheckRep.Status.Healthy

  override val healthChecks: Map<String, HealthCheck> =
    mapOf(
      "server" to HealthCheck(::serverHealthCheck),
      "test" to HealthCheck(::testHealthCheck),
    )

  private fun testHealthCheck(): HealthCheckRep.Status =
    testStatus
}
