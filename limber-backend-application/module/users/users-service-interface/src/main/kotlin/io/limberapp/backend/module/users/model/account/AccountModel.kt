package io.limberapp.backend.module.users.model.account

import io.limberapp.backend.authorization.principal.JwtRole
import java.time.LocalDateTime
import java.util.UUID

data class AccountModel(
    val id: UUID,
    val created: LocalDateTime,
    val identityProvider: Boolean,
    val superuser: Boolean,
    val name: String
) {
    fun hasRole(role: JwtRole) = when (role) {
        JwtRole.IDENTITY_PROVIDER -> identityProvider
        JwtRole.SUPERUSER -> superuser
    }
}
