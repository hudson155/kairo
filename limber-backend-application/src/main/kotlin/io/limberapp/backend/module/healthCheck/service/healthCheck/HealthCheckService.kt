package io.limberapp.backend.module.healthCheck.service.healthCheck

import io.limberapp.backend.module.healthCheck.model.healthCheck.HealthCheckModel

internal interface HealthCheckService {

    fun healthCheck(): HealthCheckModel
}
