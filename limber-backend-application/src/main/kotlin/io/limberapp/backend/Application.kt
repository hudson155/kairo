package io.limberapp.backend

import io.ktor.application.Application
import io.ktor.server.cio.EngineMain

/**
 * Main entry point for the entire application.
 */
internal fun main(args: Array<String>) = EngineMain.main(args)

/**
 * Application configuration method, used automatically by Ktor to configure and set up the
 * application.
 */
@Suppress("Unused")
internal fun Application.main() = LimberAppMonolith().bindToApplication(this)
