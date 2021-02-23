package io.limberapp.rep.healthCheck

import io.limberapp.rep.CompleteRep

object HealthCheckRep {
  enum class State {
    HEALTHY,
    UNHEALTHY;
  }

  data class Complete(
      val state: State,
  ) : CompleteRep
}
