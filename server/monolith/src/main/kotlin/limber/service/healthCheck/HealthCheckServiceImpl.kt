package limber.service.healthCheck

import com.google.inject.Inject
import limber.client.healthCheck.HealthCheckClient
import limber.feature.sql.handle
import limber.model.healthCheck.HealthCheck
import limber.rep.healthCheck.HealthCheckRep
import org.jdbi.v3.core.Jdbi

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
