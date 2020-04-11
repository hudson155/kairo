package io.limberapp.backend.module.auth.rep.accessToken

import com.piperframework.rep.CompleteRep
import com.piperframework.types.LocalDateTime
import com.piperframework.types.UUID

object AccessTokenRep {

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
