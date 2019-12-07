package io.limberapp.backend.module.auth.rep.personalAccessToken

import com.piperframework.rep.CompleteRep
import java.time.LocalDateTime
import java.util.UUID

object PersonalAccessTokenRep {

    data class OneTimeUse(
        override val id: UUID,
        override val created: LocalDateTime,
        val userId: UUID,
        val token: String
    ) : CompleteRep

    data class Complete(
        override val id: UUID,
        override val created: LocalDateTime,
        val userId: UUID
    ) : CompleteRep
}
