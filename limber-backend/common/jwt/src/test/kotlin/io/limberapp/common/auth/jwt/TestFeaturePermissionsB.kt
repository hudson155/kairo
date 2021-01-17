package io.limberapp.common.auth.jwt

import io.limberapp.common.permissions.Permission
import io.limberapp.common.permissions.Permissions
import io.limberapp.common.permissions.featurePermissions.FeaturePermissions

internal data class TestFeaturePermissionsB(
    private val permissions: Set<TestFeaturePermissionB>,
) : FeaturePermissions() {
  override val prefix: Char = 'B'

  override fun contains(permission: Permission): Boolean = permission in permissions

  override fun asBooleanList(): List<Boolean> = values.map { it in permissions }

  companion object : Permissions.Companion<TestFeaturePermissionB, TestFeaturePermissionsB>(
      values = TestFeaturePermissionB.values(),
  ) {
    override fun fromBooleanList(booleanList: List<Boolean>): TestFeaturePermissionsB =
        TestFeaturePermissionsB(from(booleanList))
  }
}
