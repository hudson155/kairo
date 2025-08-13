package kairo.feature

/**
 * Features are the core building block of a Kairo Server.
 * A Feature defines an encapsulated set of functionality,
 * such as support for a specific database or managed service,
 * or a domain-specific set of functionality.
 */
public abstract class Feature {
  /**
   * Feature names should be unique for debugging purposes,
   * but they don't strictly need to be.
   */
  public abstract val name: String

  /**
   * Called when a lifecycle event is fired.
   * This method is called concurrently with other Features,
   * so IT MUST BE THREAD-SAFE.
   */
  public open suspend fun on(event: LifecycleEvent): Unit =
    Unit

  override fun toString(): String =
    "Feature(name='$name')"
}
