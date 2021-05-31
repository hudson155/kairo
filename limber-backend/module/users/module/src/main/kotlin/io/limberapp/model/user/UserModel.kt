package io.limberapp.model.user

import io.limberapp.permissions.limber.LimberPermissions
import java.time.ZonedDateTime
import java.util.UUID

data class UserModel(
    val guid: UUID,
    val createdDate: ZonedDateTime,
    val permissions: LimberPermissions,
    val orgGuid: UUID,
    val emailAddress: String,
    val fullName: String,
    val profilePhotoUrl: String?,
) {
  data class Update(
      val permissions: LimberPermissions?,
      val fullName: String?,
  )
}
