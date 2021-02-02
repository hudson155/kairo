package io.limberapp.backend.mapper.healthCheck

import com.google.inject.Inject
import io.limberapp.backend.model.healthCheck.HealthCheckModel
import io.limberapp.backend.rep.healthCheck.HealthCheckRep

internal class HealthCheckMapper @Inject constructor() {
  fun completeRep(model: HealthCheckModel): HealthCheckRep.Complete = HealthCheckRep.Complete(
      state = when (model) {
        is HealthCheckModel.Healthy -> HealthCheckRep.State.HEALTHY
        is HealthCheckModel.Unhealthy -> HealthCheckRep.State.UNHEALTHY
      },
  )
}
