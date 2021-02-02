package io.limberapp.common.permissions.featurePermissions

import io.limberapp.common.permissions.Permission
import io.limberapp.common.permissions.Permissions

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
