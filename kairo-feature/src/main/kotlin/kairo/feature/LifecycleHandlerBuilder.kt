package kairo.feature

public class LifecycleHandlerBuilder internal constructor() {
  internal var start: (suspend (features: List<Feature>) -> Unit)? = null
  internal var stop: (suspend (features: List<Feature>) -> Unit)? = null

  public fun start(block: suspend (features: List<Feature>) -> Unit) {
    require(start == null)
    start = block
  }

  public fun stop(block: suspend (features: List<Feature>) -> Unit) {
    require(stop == null)
    stop = block
  }
}
