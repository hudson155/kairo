package kairo.feature

public class LifecycleHandler internal constructor(
  public val priority: Int,
  private val handleStart: (suspend (features: List<Feature>) -> Unit)?,
  private val handleStop: (suspend (features: List<Feature>) -> Unit)?,
) {
  /**
   * Does the work of starting each Feature.
   *
   * This method is called concurrently with other Features of the same [priority],
   * so IT MUST BE THREAD/COROUTINE-SAFE.
   */
  public suspend fun start(features: List<Feature>) {
    handleStart ?: return
    handleStart(features)
  }

  /**
   * Does the work of stopping each Feature.
   *
   * This method is called concurrently with other Features of the same [priority],
   * so IT MUST BE THREAD/COROUTINE-SAFE.
   */
  public suspend fun stop(features: List<Feature>) {
    handleStop ?: return
    handleStop(features)
  }
}
