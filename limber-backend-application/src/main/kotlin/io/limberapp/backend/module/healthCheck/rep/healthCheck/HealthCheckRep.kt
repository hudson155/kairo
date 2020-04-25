package io.limberapp.backend.module.healthCheck.rep.healthCheck

import com.piperframework.rep.CompleteRep
import java.time.LocalDateTime

internal object HealthCheckRep {
    enum class State {
        HEALTHY,
        UNHEALTHY;
    }

    data class Complete(
        override val created: LocalDateTime,
        val state: State
    ) : CompleteRep
}
