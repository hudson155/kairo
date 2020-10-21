package io.limberapp.common.module.healthCheck.rep.healthCheck

import io.limberapp.common.rep.CompleteRep
import java.time.ZonedDateTime

internal object HealthCheckRep {
  enum class State {
    HEALTHY,
    UNHEALTHY;
  }

  data class Complete(
      override val createdDate: ZonedDateTime,
      val state: State,
  ) : CompleteRep
}
