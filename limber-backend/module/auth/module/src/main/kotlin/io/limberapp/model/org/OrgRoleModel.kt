package io.limberapp.model.org

import io.limberapp.permissions.org.OrgPermissions
import io.limberapp.util.url.slugify
import java.time.ZonedDateTime
import java.util.UUID

data class OrgRoleModel(
    val guid: UUID,
    val createdDate: ZonedDateTime,
    val orgGuid: UUID,
    val name: String,
    val permissions: OrgPermissions,
    val isDefault: Boolean,
    val memberCount: Int,
) {
  val slug: String = slugify(name)

  data class Update(
      val name: String?,
      val permissions: OrgPermissions?,
      val isDefault: Boolean?,
  )
}
