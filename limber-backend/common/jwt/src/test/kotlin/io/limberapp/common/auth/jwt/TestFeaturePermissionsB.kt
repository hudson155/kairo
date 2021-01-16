package io.limberapp.common.auth.jwt

import io.limberapp.common.permissions.featurePermissions.FeaturePermissions

internal data class TestFeaturePermissionsB(
    private val permissions: Set<TestFeaturePermissionB>,
) : FeaturePermissions<TestFeaturePermissionB>(permissions) {
  override val prefix: Char = Companion.prefix

  override fun asBooleanList(): List<Boolean> = values.map { it in this }

  companion object : FeaturePermissions.Companion<TestFeaturePermissionB, TestFeaturePermissionsB>(
      values = TestFeaturePermissionB.values(),
  ) {
    override val prefix: Char = 'B'

    override fun fromBooleanList(booleanList: List<Boolean>): TestFeaturePermissionsB =
        TestFeaturePermissionsB(from(booleanList))
  }
}
