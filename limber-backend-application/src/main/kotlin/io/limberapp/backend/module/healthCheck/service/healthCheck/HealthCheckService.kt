package io.limberapp.backend.module.healthCheck.service.healthCheck

import io.limberapp.backend.module.healthCheck.model.healthCheck.HealthCheckModel

interface HealthCheckService {

    fun healthCheck(): HealthCheckModel
}
