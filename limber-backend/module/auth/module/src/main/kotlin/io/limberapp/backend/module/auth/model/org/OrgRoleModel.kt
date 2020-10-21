package io.limberapp.backend.module.auth.model.org

import io.limberapp.common.permissions.orgPermissions.OrgPermissions
import io.limberapp.common.util.url.slugify
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
  val slug = name.slugify()

  data class Update(
      val name: String?,
      val permissions: OrgPermissions?,
      val isDefault: Boolean?,
  )
}
