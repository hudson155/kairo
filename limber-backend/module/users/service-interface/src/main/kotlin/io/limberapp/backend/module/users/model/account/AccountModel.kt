package io.limberapp.backend.module.users.model.account

import io.limberapp.backend.authorization.principal.JwtRole
import java.time.LocalDateTime
import java.util.*

data class AccountModel(
  val guid: UUID,
  val createdDate: LocalDateTime,
  val identityProvider: Boolean,
  val superuser: Boolean,
) {
  fun hasRole(role: JwtRole) = when (role) {
    JwtRole.IDENTITY_PROVIDER -> identityProvider
    JwtRole.SUPERUSER -> superuser
  }
}
