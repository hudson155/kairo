package io.limberapp.backend.module.users.model.account

import io.limberapp.backend.authorization.principal.JwtRole
import java.time.LocalDateTime
import java.util.UUID

data class AccountModel(
    val id: UUID,
    val created: LocalDateTime,
    val name: String,
    val identityProvider: Boolean,
    val superuser: Boolean
) {
    fun hasRole(role: JwtRole) = when (role) {
        JwtRole.IDENTITY_PROVIDER -> identityProvider
        JwtRole.SUPERUSER -> superuser
    }
}
