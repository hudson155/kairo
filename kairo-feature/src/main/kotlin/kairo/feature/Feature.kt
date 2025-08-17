package kairo.feature

/**
 * Features are the core building block of Kairo Servers.
 * A Feature defines an encapsulated set of functionality,
 * such as support for a specific database or managed service,
 * or a domain-specific set of functionality.
 */
public abstract class Feature : LifecycleEventListener {
  /**
   * Feature names should be unique for debugging purposes,
   * but they don't strictly need to be.
   */
  public abstract val name: String

  override fun toString(): String =
    "Feature(name='$name')"
}
