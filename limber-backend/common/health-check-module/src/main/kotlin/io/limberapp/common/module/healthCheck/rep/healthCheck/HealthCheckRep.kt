package io.limberapp.common.module.healthCheck.rep.healthCheck

import io.limberapp.common.rep.CompleteRep
import io.limberapp.common.serialization.serializer.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

internal object HealthCheckRep {
  enum class State {
    HEALTHY,
    UNHEALTHY;
  }

  @Serializable
  data class Complete(
    @Serializable(with = LocalDateTimeSerializer::class)
    override val createdDate: LocalDateTime,
    val state: State,
  ) : CompleteRep
}
