package io.limberapp.permissions.feature

internal enum class TestFeaturePermission(
    override val index: Int,
    override val title: String,
    override val description: String,
) : FeaturePermission {
  TEST_FEATURE_PERMISSION_1(
      index = 0,
      title = "Test feature permission 1",
      description = "This is the first test feature permission.",
  ),
  TEST_FEATURE_PERMISSION_2(
      index = 1,
      title = "Test feature permission 2",
      description = "This is the second test feature permission.",
  );
}
