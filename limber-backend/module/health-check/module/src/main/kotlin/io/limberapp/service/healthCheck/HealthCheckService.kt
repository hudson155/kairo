package io.limberapp.service.healthCheck

import io.limberapp.model.healthCheck.HealthCheckModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class HealthCheckService {
  private val logger: Logger = LoggerFactory.getLogger(HealthCheckService::class.java)

  abstract fun healthCheck(): HealthCheckModel

  @Suppress("TooGenericExceptionCaught")
  protected fun healthTry(
      name: String,
      function: () -> Unit,
  ): HealthCheckModel.Unhealthy? {
    try {
      function()
    } catch (e: Exception) {
      logger.warn("$name health check failed.", e)
      return HealthCheckModel.Unhealthy("$name health check failed.", e)
    }
    return null
  }
}
