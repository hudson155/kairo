package io.limberapp.backend.module.healthCheck.rep.healthCheck

import com.piperframework.rep.CompleteRep
import com.piperframework.serialization.serializer.LocalDateTimeSerializer
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
    val state: State
  ) : CompleteRep
}
