package io.limberapp.backend.module.auth.model.accessToken

import java.time.LocalDateTime
import java.util.UUID

data class AccessTokenModel(
    val id: UUID,
    val created: LocalDateTime,
    val userId: UUID,
    val token: String
)
