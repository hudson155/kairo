package kairo.feature

/**
 * These methods are called when a lifecycle event is fired.
 */
public interface LifecycleEventListener {
  /**
   * Does the work of starting each Feature.
   *
   * This method is called concurrently with other Features,
   * so IT MUST BE THREAD/COROUTINE-SAFE.
   */
  public suspend fun start(features: List<Feature>): Unit =
    Unit

  /**
   * Informs Features that start is complete.
   * Should only do trivial work (not suspend).
   * Most Features shouldn't need this.
   */
  public fun afterStart(): Unit =
    Unit

  /**
   * Informs Features that stop is about to be called.
   * Should only do trivial work (not suspend).
   * Most Features shouldn't need this.
   */
  public fun beforeStop(): Unit =
    Unit

  /**
   * Does the work of stopping each Feature.
   *
   * This method is called concurrently with other Features,
   * so IT MUST BE THREAD/COROUTINE-SAFE.
   */
  public suspend fun stop(): Unit =
    Unit
}
