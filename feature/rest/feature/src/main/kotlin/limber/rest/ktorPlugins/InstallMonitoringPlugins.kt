package limber.rest.ktorPlugins

import io.ktor.server.application.Application
import io.ktor.server.application.install

internal fun Application.installMonitoringPlugins() {
  install(CallStartTime)
}
