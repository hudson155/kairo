package kairo.server

/**
 * [startupDelayMs] and [shutdownDelayMs] add extra buffer time
 * for state change and initialization during startup and shutdown.
 */
public data class FeatureManagerConfig(
  val startupDelayMs: Long,
  val shutdownDelayMs: Long,
)
