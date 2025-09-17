package kairo.feature

public class FeatureLifecycle internal constructor() {
  @Suppress("UseDataClass")
  public class Handler(
    public val priority: Int,
    public val start: suspend (features: List<Feature>) -> Unit,
    public val stop: suspend (features: List<Feature>) -> Unit,
  )

  public var handlers: List<Handler> = emptyList()
    private set

  public operator fun invoke(
    priority: Int = FeaturePriority.default,
    start: suspend (features: List<Feature>) -> Unit = {},
    stop: suspend (features: List<Feature>) -> Unit = {},
  ) {
    handlers += Handler(priority, start, stop)
  }
}
