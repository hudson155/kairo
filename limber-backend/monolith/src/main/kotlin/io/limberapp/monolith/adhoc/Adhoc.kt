package io.limberapp.monolith.adhoc

import io.ktor.application.Application
import io.limberapp.common.shutDown

internal class Adhoc(val application: Application)

@Suppress("TooGenericExceptionCaught")
internal fun Application.adhoc(function: Adhoc.() -> Unit) {
  try {
    Adhoc(this).function()
  } catch (e: Throwable) {
    shutDown(1)
    throw e
  }
}
