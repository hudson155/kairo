package io.limberapp.permissions.feature

import io.limberapp.permissions.Permission
import io.limberapp.permissions.Permissions

internal data class TestFeaturePermissionsB(
    private val permissions: Set<TestFeaturePermissionB>,
) : FeaturePermissions() {
  companion object : Permissions.Companion<TestFeaturePermissionB, TestFeaturePermissionsB>(
      values = TestFeaturePermissionB.values(),
  ) {
    override fun fromBooleanList(booleanList: List<Boolean>): TestFeaturePermissionsB =
        TestFeaturePermissionsB(from(booleanList))
  }

  override val prefix: Char = 'B'

  override fun contains(permission: Permission): Boolean = permission in permissions

  override fun asBooleanList(): List<Boolean> = values.map { it in permissions }
}
