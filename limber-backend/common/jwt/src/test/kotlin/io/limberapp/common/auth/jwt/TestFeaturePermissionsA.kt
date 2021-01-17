package io.limberapp.common.auth.jwt

import io.limberapp.common.permissions.Permission
import io.limberapp.common.permissions.Permissions
import io.limberapp.common.permissions.featurePermissions.FeaturePermissions

internal data class TestFeaturePermissionsA(
    private val permissions: Set<TestFeaturePermissionA>,
) : FeaturePermissions() {
  override val prefix: Char = 'A'

  override fun contains(permission: Permission): Boolean = permission in permissions

  override fun asBooleanList(): List<Boolean> = values.map { it in permissions }

  companion object : Permissions.Companion<TestFeaturePermissionA, TestFeaturePermissionsA>(
      values = TestFeaturePermissionA.values(),
  ) {
    override fun fromBooleanList(booleanList: List<Boolean>): TestFeaturePermissionsA =
        TestFeaturePermissionsA(from(booleanList))
  }
}