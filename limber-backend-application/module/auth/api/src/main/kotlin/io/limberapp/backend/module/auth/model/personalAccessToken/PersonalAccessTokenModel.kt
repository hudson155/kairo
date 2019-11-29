package io.limberapp.backend.module.auth.model.personalAccessToken

import java.time.LocalDateTime
import java.util.UUID

data class PersonalAccessTokenModel(
    val id: UUID,
    val created: LocalDateTime,
    val userId: UUID,
    val token: String
)
