package io.limberapp.backend

import io.ktor.application.Application
import io.limberapp.backend.adhoc.LimberAdhoc

private const val LIMBER_TASK = "LIMBER_TASK"

/**
 * Application configuration method, used automatically by Ktor to configure and set up the application.
 */
@Suppress("Unused")
internal fun Application.main() {
  when (System.getenv(LIMBER_TASK)) {
    "dbReset" -> LimberAdhoc.dbReset(this)
    null -> LimberAppMonolith(this)
  }
}
