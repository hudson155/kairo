package limber.feature.rest.ktorPlugins

import io.ktor.server.application.Application

/**
 * The list of plugins can be seen on the project generator site, https://start.ktor.io/.
 * The categories below match the categories from that site.
 */
internal fun Application.installPlugins() {
  installSecurityPlugins()
  installRoutingPlugins()
  installHttpPlugins()
  installMonitoringPlugins()
}
