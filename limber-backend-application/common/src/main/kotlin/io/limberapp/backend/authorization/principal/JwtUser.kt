package io.limberapp.backend.authorization.principal

import java.util.UUID

data class JwtUser(
    val id: UUID,
    val firstName: String?,
    val lastName: String?
)
