package io.limberapp.common.permissions.limberPermissions

import io.limberapp.common.permissions.Permission
import io.limberapp.common.permissions.Permissions

data class LimberPermissions(
    private val permissions: Set<LimberPermission>,
) : Permissions() {
  override fun contains(permission: Permission): Boolean = permission in permissions

  override fun asBooleanList(): List<Boolean> = values.map { it in permissions }

  operator fun plus(permission: LimberPermission): LimberPermissions =
      copy(permissions = permissions.plus(permission))

  operator fun minus(permission: LimberPermission): LimberPermissions =
      copy(permissions = permissions.minus(permission))

  companion object : Permissions.Companion<LimberPermission, LimberPermissions>(
      values = LimberPermission.values(),
  ) {
    override fun fromBooleanList(booleanList: List<Boolean>): LimberPermissions =
        LimberPermissions(from(booleanList))
  }
}