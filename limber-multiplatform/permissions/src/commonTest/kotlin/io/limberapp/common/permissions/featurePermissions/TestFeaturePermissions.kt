package io.limberapp.common.permissions.featurePermissions

import io.limberapp.common.permissions.Permission
import io.limberapp.common.permissions.Permissions

internal data class TestFeaturePermissions(
    private val permissions: Set<TestFeaturePermission>,
) : FeaturePermissions() {
  override val prefix: Char = 'T'

  override fun contains(permission: Permission): Boolean = permission in permissions

  override fun asBooleanList(): List<Boolean> = values.map { it in permissions }

  companion object : Permissions.Companion<TestFeaturePermission, TestFeaturePermissions>(
      values = TestFeaturePermission.values(),
  ) {
    override fun fromBooleanList(booleanList: List<Boolean>): TestFeaturePermissions =
        TestFeaturePermissions(from(booleanList))
  }
}
