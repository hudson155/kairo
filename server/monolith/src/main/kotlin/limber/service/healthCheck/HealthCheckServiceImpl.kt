package limber.service.healthCheck

import com.google.inject.Inject
import limber.client.healthCheck.HealthCheckClient
import limber.feature.sql.Sql
import limber.model.healthCheck.HealthCheck
import limber.rep.healthCheck.HealthCheckRep

internal class HealthCheckServiceImpl @Inject constructor(
  healthCheckClient: HealthCheckClient,
  private val sql: Sql,
) : HealthCheckService(healthCheckClient) {
  override val healthChecks: Map<String, HealthCheck> =
    mapOf(
      "http" to HealthCheck(::httpHealthCheck),
      "server" to HealthCheck(::serverHealthCheck),
      "sql" to HealthCheck(::sqlHealthCheck),
    )

  private suspend fun sqlHealthCheck(): HealthCheckRep.State =
    healthyIfNoException {
      val result = sql.sql { handle ->
        val query = handle.createQuery("select 1")
        return@sql query.mapTo(Int::class.java).single()
      }
      check(result == 1)
    }
}
