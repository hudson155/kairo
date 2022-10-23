package limber.feature.rest.ktorPlugins

import io.ktor.server.application.Application
import limber.config.rest.RestConfig

/**
 * The list of plugins can be seen on the project generator site, https://start.ktor.io/.
 * The categories below match the categories from that site.
 */
internal fun Application.installPlugins(config: RestConfig) {
  installRoutingPlugins()
  installHttpPlugins(config.allowedHosts, config.serverName)
  installMonitoringPlugins()
}
