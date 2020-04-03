package io.limberapp.backend.module.healthCheck.rep.healthCheck

import com.piperframework.rep.CompleteRep
import com.piperframework.serialization.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

internal object HealthCheckRep {

    @Serializable
    enum class State {
        HEALTHY,
        UNHEALTHY;
    }

    @Serializable
    data class Complete(
        @Serializable(with = LocalDateTimeSerializer::class)
        override val created: LocalDateTime,
        val state: State
    ) : CompleteRep
}
