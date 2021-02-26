package io.limberapp.service.healthCheck

import io.limberapp.model.healthCheck.HealthCheckModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class HealthCheckService {
  private val logger: Logger = LoggerFactory.getLogger(HealthCheckService::class.java)

  fun healthCheck(): HealthCheckModel {
    healthChecks.forEach { healthCheck ->
      @Suppress("TooGenericExceptionCaught")
      try {
        healthCheck.check()
      } catch (e: Exception) {
        logger.warn("${healthCheck.name} health check failed.", e)
        return@healthCheck HealthCheckModel.Unhealthy("${healthCheck.name} health check failed.", e)
      }
    }
    return HealthCheckModel.Healthy
  }

  abstract val healthChecks: List<HealthCheck>
}
