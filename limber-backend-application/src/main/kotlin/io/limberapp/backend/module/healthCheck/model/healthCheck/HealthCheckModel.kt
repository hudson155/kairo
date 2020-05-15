package io.limberapp.backend.module.healthCheck.model.healthCheck

internal sealed class HealthCheckModel {
  internal object HealthyHealthCheckModel : HealthCheckModel()

  internal data class UnhealthyHealthCheckModel(
    val reason: String,
    val e: Exception
  ) : HealthCheckModel()
}
