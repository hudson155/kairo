package kairo.feature

/**
 * Features are the core building block of Kairo applications.
 * Each Feature defines an encapsulated piece of functionality,
 * such as support for a specific database or managed service,
 * or a domain-specific set of functionality.
 */
public abstract class Feature {
  /**
   * Feature names should be unique for debugging purposes,
   * but they don't strictly need to be.
   */
  public abstract val name: String

  public val lifecycle: FeatureLifecycle = FeatureLifecycle()

  override fun toString(): String =
    "Feature(name='$name')"
}
