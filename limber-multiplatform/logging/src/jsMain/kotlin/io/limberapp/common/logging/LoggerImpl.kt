package io.limberapp.common.logging

object LoggerImpl : Logger {
  override fun debug(msg: String) {
    // Kotlin/JS does not know about console.debug.
    console.asDynamic().debug(msg)
  }

  override fun info(msg: String) {
    console.info(msg)
  }

  override fun warn(msg: String) {
    console.warn(msg)
  }

  override fun error(msg: String) {
    console.error(msg)
  }
}
