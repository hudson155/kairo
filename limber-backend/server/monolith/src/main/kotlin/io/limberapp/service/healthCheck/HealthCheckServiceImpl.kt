package io.limberapp.service.healthCheck

import com.google.inject.Inject
import org.jdbi.v3.core.Jdbi

internal class HealthCheckServiceImpl @Inject constructor(
    private val jdbi: Jdbi,
) : HealthCheckService() {
  override val healthChecks: List<HealthCheck> = listOf(
      SqlDatabaseHealthCheck(),
  )

  inner class SqlDatabaseHealthCheck : HealthCheck("SQL Database") {
    override fun check() {
      val result = jdbi.withHandle<Int, Exception> { handle ->
        handle.createQuery("SELECT 1")
            .mapTo(Int::class.java)
            .single()
      }
      check(result == 1)
    }
  }
}
