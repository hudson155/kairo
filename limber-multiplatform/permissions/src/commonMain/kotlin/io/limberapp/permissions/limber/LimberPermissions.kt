package io.limberapp.permissions.limber

import io.limberapp.permissions.Permission
import io.limberapp.permissions.Permissions

data class LimberPermissions(
    private val permissions: Set<LimberPermission>,
) : Permissions() {
  companion object : Permissions.Companion<LimberPermission, LimberPermissions>(
      values = LimberPermission.values(),
  ) {
    override fun fromBooleanList(booleanList: List<Boolean>): LimberPermissions =
        LimberPermissions(from(booleanList))
  }

  override fun contains(permission: Permission): Boolean = permission in permissions

  override fun asBooleanList(): List<Boolean> = values.map { it in permissions }

  operator fun plus(permission: LimberPermission): LimberPermissions =
      copy(permissions = permissions.plus(permission))

  operator fun minus(permission: LimberPermission): LimberPermissions =
      copy(permissions = permissions.minus(permission))
}
