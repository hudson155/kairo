package io.limberapp.common.module.healthCheck.rep.healthCheck

import io.limberapp.common.rep.CompleteRep
import java.time.LocalDateTime

internal object HealthCheckRep {
  enum class State {
    HEALTHY,
    UNHEALTHY;
  }

  data class Complete(
    override val createdDate: LocalDateTime,
    val state: State,
  ) : CompleteRep
}
