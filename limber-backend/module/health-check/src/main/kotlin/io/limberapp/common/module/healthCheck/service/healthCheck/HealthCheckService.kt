package io.limberapp.common.module.healthCheck.service.healthCheck

import io.limberapp.common.module.healthCheck.model.healthCheck.HealthCheckModel

abstract class HealthCheckService {
  abstract fun healthCheck(): HealthCheckModel

  @Suppress("TooGenericExceptionCaught")
  protected fun healthyIfNoException(name: String, function: () -> Unit): HealthCheckModel.UnhealthyHealthCheckModel? {
    try {
      function()
    } catch (e: Exception) {
      return HealthCheckModel.UnhealthyHealthCheckModel("$name health check failed.", e)
    }
    return null
  }
}
