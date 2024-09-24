package kairo.logging

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.Marker

/**
 * Helper to log a message but only log the exception if DEBUG-level logs are enabled.
 */
public fun KLogger.ifDebugEnabled(e: Throwable?, marker: Marker? = null): Throwable? {
  if (!isDebugEnabled(marker)) return null
  return e
}
