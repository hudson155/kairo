package io.limberapp.backend.module.users.model.account

import io.limberapp.common.permissions.AccountRole
import io.limberapp.common.util.string.joinNames
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
  val fullName = listOfNotNull(firstName, lastName).joinNames()

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
  )
}
