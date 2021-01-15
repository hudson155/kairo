package io.limberapp.common.auth.jwt

import io.limberapp.common.permissions.featurePermissions.FeaturePermissions

internal data class TestFeaturePermissionsA(
    private val permissions: Set<TestFeaturePermissionA>,
) : FeaturePermissions<TestFeaturePermissionA>(permissions) {
  override val prefix = Companion.prefix

  override fun asBooleanList() = values.map { it in this }

  companion object : FeaturePermissions.Companion<TestFeaturePermissionA, TestFeaturePermissionsA>(
      values = TestFeaturePermissionA.values(),
  ) {
    override val prefix = 'A'

    override fun fromBooleanList(booleanList: List<Boolean>) =
        TestFeaturePermissionsA(from(booleanList))
  }
}
