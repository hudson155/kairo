package io.limberapp.backend.model.healthCheck

sealed class HealthCheckModel {
  object Healthy : HealthCheckModel()

  data class Unhealthy(
      val reason: String,
      val e: Exception? = null,
  ) : HealthCheckModel()
}
