package io.limberapp.auth.jwt

import java.util.UUID

data class JwtUser(
    val guid: UUID,
    val fullName: String,
)
