package kairo.feature

public class LifecycleBuilder internal constructor() {
  internal var handlers: List<LifecycleHandler> = emptyList()
    private set

  public fun handler(
    priority: Int = FeaturePriority.default,
    block: LifecycleHandlerBuilder.() -> Unit,
  ) {
    val builder = LifecycleHandlerBuilder().apply(block)
    handlers += LifecycleHandler(
      priority = priority,
      handleStart = builder.start,
      handleStop = builder.stop,
    )
  }
}
