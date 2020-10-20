package io.limberapp.backend.module.users.model.account

import io.limberapp.permissions.AccountRole
import java.time.ZonedDateTime
import java.util.*

data class UserModel(
    val guid: UUID,
    val createdDate: ZonedDateTime,
    val identityProvider: Boolean,
    val superuser: Boolean,
    val orgGuid: UUID,
    val firstName: String?,
    val lastName: String?,
    val emailAddress: String,
    val profilePhotoUrl: String?,
) {
  fun hasRole(role: AccountRole) = when (role) {
    AccountRole.IDENTITY_PROVIDER -> identityProvider
    AccountRole.LIMBER_SERVER -> false // Users can't be servers.
    AccountRole.SUPERUSER -> superuser
  }

  data class Update(
      val identityProvider: Boolean?,
      val superuser: Boolean?,
      val firstName: String?,
      val lastName: String?,
  ) {
    companion object {
      fun fromRole(role: AccountRole, value: Boolean) = Update(
          identityProvider = if (role == AccountRole.IDENTITY_PROVIDER) value else null,
          superuser = if (role == AccountRole.SUPERUSER) value else null,
          firstName = null,
          lastName = null
      )
    }
  }
}
