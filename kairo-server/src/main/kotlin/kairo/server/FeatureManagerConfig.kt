package kairo.server

public data class FeatureManagerConfig(
  val lifecycle: Lifecycle,
) {
  /**
   * [startupDelayMs] and [shutdownDelayMs] add extra buffer time
   * for state change and initialization during startup and shutdown.
   */
  public data class Lifecycle(
    val startupDelayMs: Long,
    val shutdownDelayMs: Long,
  )
}
