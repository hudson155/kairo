package io.limberapp.backend.module.auth.rep.accessToken

import com.piperframework.rep.CompleteRep
import java.time.LocalDateTime
import java.util.UUID

internal object AccessTokenRep {

    data class OneTimeUse(
        val id: UUID,
        override val created: LocalDateTime,
        val userId: UUID,
        val token: String
    ) : CompleteRep

    data class Complete(
        val id: UUID,
        override val created: LocalDateTime,
        val userId: UUID
    ) : CompleteRep
}
