package io.limberapp.module.users.model.user

import io.limberapp.permissions.limber.LimberPermissions
import io.limberapp.util.string.fullName
import java.time.ZonedDateTime
import java.util.UUID

data class UserModel(
    val guid: UUID,
    val createdDate: ZonedDateTime,
    val permissions: LimberPermissions,
    val orgGuid: UUID,
    val emailAddress: String,
    val firstName: String,
    val lastName: String,
    val profilePhotoUrl: String?,
) {
  val fullName: String = fullName(firstName, lastName)

  data class Update(
      val permissions: LimberPermissions?,
      val firstName: String?,
      val lastName: String?,
  )
}
