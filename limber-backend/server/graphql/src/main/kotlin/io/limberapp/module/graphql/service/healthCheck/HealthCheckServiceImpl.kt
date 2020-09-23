package io.limberapp.module.graphql.service.healthCheck

import com.google.inject.Inject
import io.limberapp.common.module.healthCheck.model.healthCheck.HealthCheckModel
import io.limberapp.common.module.healthCheck.service.healthCheck.HealthCheckService

internal class HealthCheckServiceImpl @Inject constructor() : HealthCheckService() {
  override fun healthCheck(): HealthCheckModel {
    checkConnectionToMonolith()?.let { return it }
    return HealthCheckModel.HealthyHealthCheckModel
  }

  private fun checkConnectionToMonolith() = healthyIfNoException("Monolith") {
    // TODO
  }
}
