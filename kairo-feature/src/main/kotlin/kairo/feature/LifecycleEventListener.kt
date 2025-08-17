package kairo.feature

/**
 * These methods are called when a lifecycle event is fired.
 * These methods are called concurrently with other Features,
 * so THEY MUST BE THREAD/COROUTINE-SAFE.
 */
public interface LifecycleEventListener {
  public suspend fun start(features: List<Feature>): Unit =
    Unit

  public suspend fun stop(): Unit =
    Unit
}
