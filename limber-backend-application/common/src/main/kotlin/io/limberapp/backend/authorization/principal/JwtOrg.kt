package io.limberapp.backend.authorization.principal

import java.util.UUID

data class JwtOrg(
    val id: UUID,
    val name: String,
    val featureIds: Set<UUID>
)
