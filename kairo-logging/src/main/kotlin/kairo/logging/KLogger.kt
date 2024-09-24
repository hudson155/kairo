package kairo.logging

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.Marker

public fun KLogger.ifDebugEnabled(e: Throwable?, marker: Marker? = null): Throwable? {
  if (!isDebugEnabled(marker)) return null
  return e
}
