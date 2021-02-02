package io.limberapp.backend.rep.healthCheck

import io.limberapp.common.rep.CompleteRep

object HealthCheckRep {
  enum class State {
    HEALTHY,
    UNHEALTHY;
  }

  data class Complete(
      val state: State,
  ) : CompleteRep
}
