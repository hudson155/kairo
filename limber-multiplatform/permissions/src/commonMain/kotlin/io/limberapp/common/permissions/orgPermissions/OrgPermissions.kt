package io.limberapp.common.permissions.orgPermissions

import io.limberapp.common.permissions.Permission
import io.limberapp.common.permissions.Permissions

data class OrgPermissions(
    private val permissions: Set<OrgPermission>,
) : Permissions(), Set<OrgPermission> by permissions {
  companion object : Permissions.Companion<OrgPermission, OrgPermissions>(
      values = OrgPermission.values(),
  ) {
    override fun fromBooleanList(booleanList: List<Boolean>): OrgPermissions =
        OrgPermissions(from(booleanList))
  }

  override fun contains(permission: Permission): Boolean = permission in permissions

  override fun asBooleanList(): List<Boolean> = values.map { it in permissions }

  operator fun plus(permission: OrgPermission): OrgPermissions =
      copy(permissions = permissions.plus(permission))

  operator fun minus(permission: OrgPermission): OrgPermissions =
      copy(permissions = permissions.minus(permission))
}
