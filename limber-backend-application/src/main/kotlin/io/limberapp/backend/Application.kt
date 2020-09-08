package io.limberapp.backend

import io.ktor.application.Application
import io.limberapp.backend.adhoc.Adhoc
import io.limberapp.backend.adhoc.adhoc
import io.limberapp.backend.adhoc.dbReset

private const val LIMBER_TASK = "LIMBER_TASK"

/**
 * Application configuration method, used automatically by Ktor to configure and set up the application.
 */
@Suppress("Unused")
internal fun Application.main() {
  when (System.getenv(LIMBER_TASK)) {
    "dbReset" -> adhoc(Adhoc::dbReset)
    null -> LimberAppMonolith(this)
  }
}
