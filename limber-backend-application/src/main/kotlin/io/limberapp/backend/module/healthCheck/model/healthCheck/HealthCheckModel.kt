package io.limberapp.backend.module.healthCheck.model.healthCheck

internal sealed class HealthCheckModel {

    object HealthyHealthCheckModel : HealthCheckModel()

    data class UnhealthyHealthCheckModel(
        val reason: String,
        val e: Exception
    ) : HealthCheckModel()
}
