package io.limberapp.backend.module.users.model.account

import io.limberapp.permissions.AccountRole
import java.time.LocalDateTime
import java.util.*

data class AccountModel(
    val guid: UUID,
    val createdDate: LocalDateTime,
    val identityProvider: Boolean,
    val superuser: Boolean,
) {
  fun hasRole(role: AccountRole) = when (role) {
    AccountRole.IDENTITY_PROVIDER -> identityProvider
    AccountRole.SUPERUSER -> superuser
  }
}
