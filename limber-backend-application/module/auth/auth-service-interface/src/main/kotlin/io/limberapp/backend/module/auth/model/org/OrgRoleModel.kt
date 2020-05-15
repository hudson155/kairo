package io.limberapp.backend.module.auth.model.org

import io.limberapp.backend.authorization.permissions.OrgPermissions
import java.time.LocalDateTime
import java.util.UUID

data class OrgRoleModel(
  val guid: UUID,
  val createdDate: LocalDateTime,
  val orgGuid: UUID,
  val name: String,
  val permissions: OrgPermissions,
  val memberCount: Int
) {
  data class Update(
    val name: String?,
    val permissions: OrgPermissions?
  )
}
