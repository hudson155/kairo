package io.limberapp.common.permissions.orgPermissions

import io.limberapp.common.permissions.Permissions

data class OrgPermissions(
    private val permissions: Set<OrgPermission>,
) : Permissions<OrgPermission>(permissions) {
  override fun asBooleanList() = values.map { it in this }

  companion object : Permissions.Companion<OrgPermission, OrgPermissions>(
      values = OrgPermission.values(),
  ) {
    override fun fromBooleanList(booleanList: List<Boolean>) =
        OrgPermissions(from(booleanList))
  }
}
