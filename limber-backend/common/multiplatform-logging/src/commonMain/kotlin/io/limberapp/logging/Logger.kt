package io.limberapp.logging

/**
 * Simple multiplatform logging facade, backed by platform-specific implementations.
 */
expect interface Logger {
  fun debug(msg: String)
  fun info(msg: String)
  fun warn(msg: String)
  fun error(msg: String)
}
