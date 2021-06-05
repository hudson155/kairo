package io.limberapp.permissions.feature

internal enum class TestFeaturePermissionB(
    override val index: Int,
    override val title: String,
    override val description: String,
) : FeaturePermission {
  TEST_FEATURE_PERMISSION_B1(
      index = 0,
      title = "Test feature permission B1",
      description = "This is the first test feature permission for B.",
  ),
  TEST_FEATURE_PERMISSION_B2(
      index = 1,
      title = "Test feature permission B2",
      description = "This is the second test feature permission B.",
  );
}
