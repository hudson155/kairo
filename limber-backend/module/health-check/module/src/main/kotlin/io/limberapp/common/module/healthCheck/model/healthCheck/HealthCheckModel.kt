package io.limberapp.common.module.healthCheck.model.healthCheck

sealed class HealthCheckModel {
  object HealthyHealthCheckModel : HealthCheckModel()

  data class UnhealthyHealthCheckModel(
      val reason: String,
      val e: Exception,
  ) : HealthCheckModel()
}
