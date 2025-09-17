package kairo.feature

@Suppress("UseDataClass")
public class LifecycleHandler internal constructor(
  public val priority: Int,
  private val handleStart: (suspend (features: List<Feature>) -> Unit)?,
  private val handleStop: (suspend (features: List<Feature>) -> Unit)?,
) {
  public suspend fun start(features: List<Feature>) {
    handleStart ?: return
    handleStart(features)
  }

  public suspend fun stop(features: List<Feature>) {
    handleStop ?: return
    handleStop(features)
  }
}
