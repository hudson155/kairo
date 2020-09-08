package io.limberapp.backend.adhoc

import com.piperframework.shutDown
import io.ktor.application.Application

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
