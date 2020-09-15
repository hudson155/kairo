package io.limberapp.common.module.healthCheck.service.healthCheck

import io.limberapp.common.module.healthCheck.model.healthCheck.HealthCheckModel

interface HealthCheckService {
  fun healthCheck(): HealthCheckModel
}
