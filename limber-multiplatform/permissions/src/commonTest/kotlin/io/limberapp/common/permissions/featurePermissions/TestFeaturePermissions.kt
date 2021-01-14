package io.limberapp.common.permissions.featurePermissions

internal data class TestFeaturePermissions(
    private val permissions: Set<TestFeaturePermission>,
) : FeaturePermissions<TestFeaturePermission>(permissions) {
  override val prefix = Companion.prefix

  override fun asBooleanList() = values.map { it in this }

  companion object : FeaturePermissions.Companion<TestFeaturePermission, TestFeaturePermissions>(
      values = TestFeaturePermission.values(),
  ) {
    override val prefix = 'T'

    override fun fromBooleanList(booleanList: List<Boolean>) =
        TestFeaturePermissions(from(booleanList))
  }
}
