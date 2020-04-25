package io.limberapp.backend.module.auth.rep.accessToken

import com.piperframework.rep.CompleteRep
import com.piperframework.serialization.serializer.LocalDateTimeSerializer
import com.piperframework.serialization.serializer.UuidSerializer
import com.piperframework.types.LocalDateTime
import com.piperframework.types.UUID
import kotlinx.serialization.Serializable

object AccessTokenRep {
    @Serializable
    data class OneTimeUse(
        @Serializable(with = UuidSerializer::class)
        val id: UUID,
        @Serializable(with = LocalDateTimeSerializer::class)
        override val created: LocalDateTime,
        @Serializable(with = UuidSerializer::class)
        val userId: UUID,
        val token: String
    ) : CompleteRep

    @Serializable
    data class Complete(
        @Serializable(with = UuidSerializer::class)
        val id: UUID,
        @Serializable(with = LocalDateTimeSerializer::class)
        override val created: LocalDateTime,
        @Serializable(with = UuidSerializer::class)
        val userId: UUID
    ) : CompleteRep
}
