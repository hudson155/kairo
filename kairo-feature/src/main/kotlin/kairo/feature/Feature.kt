package kairo.feature

/**
 * TODO: Add KDoc.
 */
public abstract class Feature {
  /**
   * Feature names should be unique.
   */
  public abstract val name: String

  /**
   * Called during various lifecycle events.
   * This method is called concurrently with other Features.
   */
  public open suspend fun on(event: LifecycleEvent): Unit =
    Unit
}
