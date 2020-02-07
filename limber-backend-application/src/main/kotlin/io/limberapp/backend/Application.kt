package io.limberapp.backend

import io.ktor.application.Application
import io.ktor.server.cio.EngineMain

/**
 * Main entry point to run the application locally.
 */
internal fun main(args: Array<String>) = EngineMain.main(args)

/**
 * Application configuration method, used automatically by Ktor to configure and set up the application. Regardless of
 * how the application is started (whether from the Main function above or some other way (e.g. Google App Engine).
 */
@Suppress("unused")
internal fun Application.main() = LimberAppMonolith(this)
