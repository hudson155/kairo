package io.limberapp.common.auth.jwt

import java.util.UUID

data class JwtUser(
    val guid: UUID,
    val firstName: String?,
    val lastName: String?,
)
