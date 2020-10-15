package io.limberapp.auth.jwt

import java.util.*

data class JwtUser(
    val guid: UUID,
    val firstName: String?,
    val lastName: String?,
)
