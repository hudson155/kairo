package io.limberapp.common.permissions.featurePermissions

internal data class TestFeaturePermissions(
    private val permissions: Set<TestFeaturePermission>,
) : FeaturePermissions<TestFeaturePermission>(permissions) {
  override val prefix: Char = Companion.prefix

  override fun asBooleanList(): List<Boolean> = values.map { it in this }

  companion object : FeaturePermissions.Companion<TestFeaturePermission, TestFeaturePermissions>(
      values = TestFeaturePermission.values(),
  ) {
    override val prefix: Char = 'T'

    override fun fromBooleanList(booleanList: List<Boolean>): TestFeaturePermissions =
        TestFeaturePermissions(from(booleanList))
  }
}
