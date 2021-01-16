package io.limberapp.common.permissions.limberPermissions

import io.limberapp.common.permissions.Permissions

data class LimberPermissions(
    private val permissions: Set<LimberPermission>,
) : Permissions<LimberPermission>(permissions) {
  override fun asBooleanList(): List<Boolean> = values.map { it in this }

  companion object : Permissions.Companion<LimberPermission, LimberPermissions>(
      values = LimberPermission.values(),
  ) {
    override fun fromBooleanList(booleanList: List<Boolean>): LimberPermissions =
        LimberPermissions(from(booleanList))
  }
}
