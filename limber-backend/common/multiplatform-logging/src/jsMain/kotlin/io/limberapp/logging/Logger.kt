package io.limberapp.logging

actual interface Logger {
  actual fun debug(msg: String)
  actual fun info(msg: String)
  actual fun warn(msg: String)
  actual fun error(msg: String)
}
