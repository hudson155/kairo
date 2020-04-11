package io.limberapp.backend.module.users.model.account

import io.limberapp.backend.authorization.principal.JwtRole
import java.time.LocalDateTime
import java.util.UUID

data class UserModel(
    val id: UUID,
    val created: LocalDateTime,
    val orgId: UUID,
    val firstName: String,
    val lastName: String,
    val emailAddress: String,
    val profilePhotoUrl: String?,
    val roles: Set<JwtRole>
) {

    data class Update(
        val firstName: String?,
        val lastName: String?
    )
}
