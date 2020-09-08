package io.limberapp.backend

import io.ktor.application.Application

/**
 * Application configuration method, used automatically by Ktor to configure and set up the application.
 */
@Suppress("Unused")
internal fun Application.main() = LimberAppMonolith(this)
