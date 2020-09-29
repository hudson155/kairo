package io.limberapp.graphqlServer.module.service.healthCheck

import com.google.inject.Inject
import io.limberapp.common.module.healthCheck.model.healthCheck.HealthCheckModel
import io.limberapp.common.module.healthCheck.service.healthCheck.HealthCheckService

internal class HealthCheckServiceImpl @Inject constructor() : HealthCheckService() {
  // TODO: Health check downstream services.
  override fun healthCheck(): HealthCheckModel = HealthCheckModel.HealthyHealthCheckModel
}
