package io.limberapp.backend.module.healthCheck.mapper.healthCheck

import com.google.inject.Inject
import io.limberapp.backend.module.healthCheck.model.healthCheck.HealthCheckModel
import io.limberapp.backend.module.healthCheck.rep.healthCheck.HealthCheckRep
import java.time.Clock
import java.time.LocalDateTime

internal class HealthCheckMapper @Inject constructor(
  private val clock: Clock,
) {
  fun completeRep(model: HealthCheckModel) = HealthCheckRep.Complete(
    createdDate = LocalDateTime.now(clock),
    state = when (model) {
      is HealthCheckModel.HealthyHealthCheckModel -> HealthCheckRep.State.HEALTHY
      is HealthCheckModel.UnhealthyHealthCheckModel -> HealthCheckRep.State.UNHEALTHY
    }
  )
}
