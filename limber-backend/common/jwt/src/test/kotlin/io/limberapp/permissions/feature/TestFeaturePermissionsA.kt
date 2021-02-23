package io.limberapp.permissions.feature

import io.limberapp.permissions.Permission
import io.limberapp.permissions.Permissions

internal data class TestFeaturePermissionsA(
    private val permissions: Set<TestFeaturePermissionA>,
) : FeaturePermissions() {
  companion object : Permissions.Companion<TestFeaturePermissionA, TestFeaturePermissionsA>(
      values = TestFeaturePermissionA.values(),
  ) {
    override fun fromBooleanList(booleanList: List<Boolean>): TestFeaturePermissionsA =
        TestFeaturePermissionsA(from(booleanList))
  }

  override val prefix: Char = 'A'

  override fun contains(permission: Permission): Boolean = permission in permissions

  override fun asBooleanList(): List<Boolean> = values.map { it in permissions }
}
