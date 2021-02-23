package io.limberapp.config

/**
 * The Config interface contains required configuration for the app to run. It doesn't contain Ktor
 * built-in configuration. A particular server module might use an implementation that adds a few
 * properties specific to that server, its features, or its other modules.
 */
interface Config {
  val authentication: AuthenticationConfig
  val clock: ClockConfig
  val uuids: UuidsConfig
}
