package io.limberapp.permissions.org

import io.limberapp.permissions.Permission
import io.limberapp.permissions.Permissions

data class OrgPermissions(
    private val permissions: Set<OrgPermission>,
) : Permissions() {
  companion object : Permissions.Companion<OrgPermission, OrgPermissions>(
      values = OrgPermission.values(),
  ) {
    override fun fromBooleanList(booleanList: List<Boolean>): OrgPermissions =
        OrgPermissions(from(booleanList))
  }

  override fun contains(permission: Permission): Boolean = permission in permissions

  override fun asBooleanList(): List<Boolean> = values.map { it in permissions }

  operator fun plus(other: OrgPermissions): OrgPermissions =
      OrgPermissions(permissions = permissions + other.permissions)

  operator fun plus(permission: OrgPermission): OrgPermissions =
      OrgPermissions(permissions = permissions + permission)

  operator fun minus(permission: OrgPermission): OrgPermissions =
      OrgPermissions(permissions = permissions - permission)
}
