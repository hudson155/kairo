package limber.config

/**
 * [startupDelayMs] and [shutdownDelayMs] add extra buffer time
 * for state change and initialization during startup and shutdown.
 */
public data class ServerConfig(
  val startupDelayMs: Long,
  val shutdownDelayMs: Long,
)
