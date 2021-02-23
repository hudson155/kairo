package io.limberapp.permissions.feature

internal enum class TestFeaturePermissionA(
    override val index: Int,
    override val title: String,
    override val description: String,
) : FeaturePermission {
  TEST_FEATURE_PERMISSION_A1(
      index = 0,
      title = "Test feature permission A1",
      description = "This is the first test feature permission for A.",
  ),
  TEST_FEATURE_PERMISSION_A2(
      index = 1,
      title = "Test feature permission A2",
      description = "This is the second test feature permission A.",
  );
}
