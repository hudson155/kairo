package io.limberapp.backend.module.auth.model.org

import io.limberapp.permissions.orgPermissions.OrgPermissions
import java.time.ZonedDateTime
import java.util.*

data class OrgRoleModel(
    val guid: UUID,
    val createdDate: ZonedDateTime,
    val orgGuid: UUID,
    val name: String,
    val permissions: OrgPermissions,
    val isDefault: Boolean,
    val memberCount: Int,
) {
  data class Update(
      val name: String?,
      val permissions: OrgPermissions?,
      val isDefault: Boolean?,
  )
}
