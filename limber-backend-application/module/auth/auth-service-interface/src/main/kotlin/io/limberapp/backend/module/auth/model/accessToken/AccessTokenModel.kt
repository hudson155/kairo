package io.limberapp.backend.module.auth.model.accessToken

import java.time.LocalDateTime
import java.util.UUID

data class AccessTokenModel(
    val guid: UUID,
    val createdDate: LocalDateTime,
    val userGuid: UUID,
    val encryptedSecret: String
)
