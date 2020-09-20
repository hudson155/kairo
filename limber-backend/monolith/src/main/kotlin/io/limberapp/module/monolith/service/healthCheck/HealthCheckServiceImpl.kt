package io.limberapp.module.monolith.service.healthCheck

import com.google.inject.Inject
import io.limberapp.common.module.healthCheck.model.healthCheck.HealthCheckModel
import io.limberapp.common.module.healthCheck.service.healthCheck.HealthCheckService
import org.jdbi.v3.core.Jdbi

internal class HealthCheckServiceImpl @Inject constructor(private val jdbi: Jdbi) : HealthCheckService() {
  override fun healthCheck(): HealthCheckModel {
    checkDatabase()?.let { return it }
    return HealthCheckModel.HealthyHealthCheckModel
  }

  private fun checkDatabase() = healthyIfNoException("Database") {
    jdbi.useHandle<Exception> { it.execute("SELECT 1") }
  }
}
