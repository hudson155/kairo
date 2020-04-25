package io.limberapp.backend.module.users.model.account

import io.limberapp.backend.authorization.principal.JwtRole
import java.time.LocalDateTime
import java.util.UUID

data class UserModel(
    val id: UUID,
    val created: LocalDateTime,
    val identityProvider: Boolean,
    val superuser: Boolean,
    val orgId: UUID,
    val firstName: String,
    val lastName: String,
    val emailAddress: String,
    val profilePhotoUrl: String?
) {
    fun hasRole(role: JwtRole) = when (role) {
        JwtRole.IDENTITY_PROVIDER -> identityProvider
        JwtRole.SUPERUSER -> superuser
    }

    data class Update(
        val identityProvider: Boolean?,
        val superuser: Boolean?,
        val firstName: String?,
        val lastName: String?
    ) {
        companion object {
            fun fromRole(role: JwtRole, value: Boolean) = Update(
                identityProvider = if (role == JwtRole.IDENTITY_PROVIDER) value else null,
                superuser = if (role == JwtRole.SUPERUSER) value else null,
                firstName = null,
                lastName = null
            )
        }
    }
}
